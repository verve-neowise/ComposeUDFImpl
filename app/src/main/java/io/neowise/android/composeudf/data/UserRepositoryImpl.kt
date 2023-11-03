package io.neowise.android.composeudf.data

import io.neowise.android.composeudf.domain.User
import io.neowise.android.composeudf.domain.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl : UserRepository {

    private val users = mutableListOf(
        User(name = "John", surname = "Doe"),
        User(name = "Jane", surname = "Smith"),
        User(name = "Michael", surname = "Johnson"),
        User(name = "Emily", surname = "Williams"),
        User(name = "William", surname = "Brown")
    )

    override fun getUsers() = flow {
        delay(2000)
        emit(users.toList())
    }

    override fun createUser(name: String, surname: String) = flow {
        delay(2000)
        users += User(name, surname)
        emit(users.last())
    }
}