package com.zabbarfalih.sipstis.data

import com.zabbarfalih.sipstis.model.ChangePasswordForm
import com.zabbarfalih.sipstis.model.User
import com.zabbarfalih.sipstis.model.LoginForm
import com.zabbarfalih.sipstis.model.LoginResponse
import com.zabbarfalih.sipstis.model.RegisterForm
import com.zabbarfalih.sipstis.service.UserService

interface UserRepository {
    suspend fun register(user: RegisterForm)
    suspend fun login(form: LoginForm): LoginResponse
    suspend fun getProfile(token: String): User
    suspend fun updateProfile(token: String, user: User): User
    suspend fun updatePassword(token: String, form: ChangePasswordForm): User
    suspend fun deleteUser(token: String)
}

class NetworkUserRepository(private val userService: UserService) : UserRepository {
    override suspend fun register(user: RegisterForm) = userService.register(user)
    override suspend fun login(form: LoginForm): LoginResponse = userService.login(form)
    override suspend fun getProfile(token: String): User = userService.getProfile("Bearer $token")
    override suspend fun updateProfile(token: String, user: User): User = userService.updateProfile("Bearer $token", user)
    override suspend fun updatePassword(token: String, form: ChangePasswordForm): User = userService.updatePassword("Bearer $token", form)
    override suspend fun deleteUser(token: String) = userService.deleteUser("Bearer $token")
}