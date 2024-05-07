package com.pabloat.GameHubConnect.data.local

/**
 * User es una clase que representa el objeto Usuario que se almacena en la base de datos de arriba.
 * @param id es el identificador del usuario.
 * @param userId es el identificador del usuario en la API.
 * @param displayName es el nombre de usuario.
 * @param avatarUrl es la URL de la imagen de perfil del usuario.
 * @param quote es la frase de perfil del usuario.
 * @param rol es el rol del usuario.
 */
data class User (
    val id:String?,
    val userId:String,
    val displayName:String,
    val avatarUrl:String,
    val quote:String,
    val rol:String
){
    fun toMap():MutableMap<String,Any>{
        return mutableMapOf(
            "user_id" to this.userId,
            "display_name" to this.displayName,
            "quote" to this.quote,
            "avatar_url" to this.avatarUrl,
            "quote" to this.quote,
            "rol" to this.rol
        )
    }
}