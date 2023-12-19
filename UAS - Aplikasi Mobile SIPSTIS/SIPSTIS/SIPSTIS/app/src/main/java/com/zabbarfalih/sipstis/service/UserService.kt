package com.zabbarfalih.sipstis.service

import com.zabbarfalih.sipstis.model.ChangePasswordForm
import com.zabbarfalih.sipstis.model.LoginForm
import com.zabbarfalih.sipstis.model.LoginResponse
import com.zabbarfalih.sipstis.model.RegisterForm
import com.zabbarfalih.sipstis.model.User
import retrofit2.http.*

interface UserService {
    @POST("/register")
    suspend fun register(@Body user: RegisterForm)

    @POST("/login")
    suspend fun login(@Body form: LoginForm): LoginResponse

    @GET("/profile")
    suspend fun getProfile(@Header("Authorization") token: String): User

    @PUT("/profile")
    suspend fun updateProfile(@Header("Authorization") token: String, @Body user: User): User

    @PATCH("/profile/password")
    suspend fun updatePassword(@Header("Authorization") token: String, @Body form: ChangePasswordForm): User

    @DELETE("/profile")
    suspend fun deleteUser(@Header("Authorization") token: String)

    // Role Admin
    @GET("/admin/user/{id}")
    suspend fun getUserByAdmin(@Header("Authorization") token: String, @Path("id") id: Long): User

    @GET("/admin/user")
    suspend fun getAllUsersByAdmin(@Header("Authorization") token: String): List<User>

    @DELETE("/admin/user/{nip}")
    suspend fun deleteUserByAdmin(@Header("Authorization") token: String, @Path("id") id: Long)

    @POST("/user/{userId}/role/{role}")
    suspend fun changeRoleUserByAdmin(@Header("Authorization") token: String, @Path("userId") userId: Long, @Path("role") role: String)

}
