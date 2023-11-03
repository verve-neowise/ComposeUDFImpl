package io.neowise.android.composeudf.domain

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<List<User>>
    fun createUser(name: String, surname: String): Flow<User>
}