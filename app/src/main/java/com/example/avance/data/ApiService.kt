package com.example.avance.data

import retrofit2.http.*

interface ApiService {

    @GET("api/usuarios")
    suspend fun getUsuarios(): List<UsuarioApi>

    @GET("api/usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: Long): UsuarioApi

    @POST("api/usuarios")
    suspend fun createUsuario(@Body usuario: UsuarioCreate): UsuarioApi

    @PUT("api/usuarios/{id}")
    suspend fun updateUsuario(
        @Path("id") id: Long,
        @Body usuario: UsuarioCreate
    ): UsuarioApi

    @DELETE("api/usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Long)
}
