package com.example.controller

import com.example.model.LoginRequest
import com.example.model.RegisterRequest
import com.example.model.User
import com.example.model.toResponse
import com.example.repository.UserRepositoryDb
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.mindrot.jbcrypt.BCrypt
import java.util.*


fun Route.userRoutes() {
    get("/") {
        call.respondText("Hello World!")
    }

    get("/users") {
        val users = UserRepositoryDb.getAll()
        call.respond(users.map {it.toResponse()})
    }
    post("/register") {
        val contentType = call.request.headers["Content-Type"]
        println("Content-Type da requisição: $contentType")

        val request = call.receive<RegisterRequest>()

        if (request.name.isBlank() || request.email.isBlank() || request.password.isBlank()) {
            call.respondText("Todos os campos são obrigatórios", status = io.ktor.http.HttpStatusCode.BadRequest)
            return@post
        }

        val existingUser = UserRepositoryDb.findByEmail(request.email)
        if (existingUser != null) {
            call.respondText("Este e-mail já está cadastrado", status = HttpStatusCode.Conflict)
            return@post
        }

        val hashedPassword = BCrypt.hashpw(request.password, BCrypt.gensalt())

        val user = User(
            id = UUID.randomUUID(),
            name = request.name,
            email = request.email,
            passwordHash = hashedPassword
        )

        UserRepositoryDb.add(user)

        call.respondText("Usuário registrado com sucesso! ID: ${user.id}", status = io.ktor.http.HttpStatusCode.Created)
    }

    post("/login") {
        val request = call.receive<LoginRequest>()

        val user = UserRepositoryDb.findByEmail(request.email)

        if (user == null) {
            call.respondText("Usuario nao encontrado", status = HttpStatusCode.NotFound)
            return@post
        }

        val passwordMatches = org.mindrot.jbcrypt.BCrypt.checkpw(request.password, user.passwordHash)

        if (!passwordMatches) {
            call.respondText("senha incorreta", status = HttpStatusCode.Unauthorized)
            return@post
        }
            call.respondText("Login bem-sucedido! Bem-vindo, ${user.name}")

    }

    delete("/users/{id}") {
        val idParam = call.parameters["id"]

        if (idParam == null) {
            call.respond(HttpStatusCode.BadRequest, "ID do usuário não informado")
            return@delete
        }

        val id = try {
            UUID.fromString(idParam)
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, "ID do uuid invalido")
            return@delete
        }

        val deleted = UserRepositoryDb.delete(id)

        if (deleted) {
            call.respondText("Usuário removido com sucesso!", status = HttpStatusCode.OK)
        } else {
            call.respondText("Usuário não encontrado", status = HttpStatusCode.NotFound)
        }
    }

}