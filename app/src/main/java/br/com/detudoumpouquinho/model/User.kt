package br.com.detudoumpouquinho.model

data class User(
    var name: String? = null,
    var email: String,
    var password: String,
    var confirmPassword: String? = null,
    var userIdentify: UserIdentify? = null
) {
    enum class UserIdentify(val identify: String) {
        USER("U")
    }
}
