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



    @GET("/api/categorias")
    suspend fun getCategorias(): List<Categoria>

    @GET("/api/categorias/{id}")
    suspend fun getCategoria(@Path("id") id: Long): Categoria

    @POST("/api/categorias")
    suspend fun createCategoria(@Body categoria: CategoriaCreate): Categoria

    @PUT("/api/categorias/{id}")
    suspend fun updateCategoria(
        @Path("id") id: Long,
        @Body categoria: CategoriaCreate
    ): Categoria

    @DELETE("/api/categorias/{id}")
    suspend fun deleteCategoria(@Path("id") id: Long)


    @POST("api/ordenes")
    suspend fun crearOrden(@Body orden: Orden): Orden

    @GET("api/ordenes")
    suspend fun obtenerTodasOrdenes(): List<Orden>

    @POST("api/ordenes/{id}/estado")
    suspend fun actualizarEstado(
        @Path("id") id: Long,
        @Body estado: String
    )
    @POST("api/usuarios/recuperar")
    suspend fun recuperarContrasena(
        @Body solicitud: SolicitudActualizarClave
    ): RespuestaApi

}
