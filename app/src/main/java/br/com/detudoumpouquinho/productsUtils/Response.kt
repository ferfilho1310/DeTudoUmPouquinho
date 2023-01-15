package br.com.detudoumpouquinho.productsUtils

sealed class Response<T>(
    val data: T? = null
) {
    class SUCCESS<T>(data: T) : Response<T>(data)
    class ERROR<T>(data: T? = null) : Response<T>(data)
    class LOADING<T> : Response<T>()
}