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

    @GET("api/productos")
    suspend fun getProductos(): List<Producto>

    @GET("api/productos/{id}")
    suspend fun getProductoById(@Path("id") id: Long): Producto

    @POST("api/productos")
    suspend fun createProducto(@Body producto: ProductoCreate): Producto


    @PUT("api/productos/{id}")
    suspend fun updateProducto(
        @Path("id") id: Long,
        @Body producto: ProductoCreate
    ): Producto

    @DELETE("api/productos/{id}")
    suspend fun deleteProducto(@Path("id") id: Long)
}
