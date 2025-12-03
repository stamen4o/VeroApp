package com.vero.app.data.remote.dto

data class LoginResponse(
    val oauth: OAuthData
)

data class OAuthData(
    val access_token: String
)
