package br.com.detudoumpouquinho.model

data class User(
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var confirmPassword: String? = null,
    var identifier: String? = null
)
