package com.keyflare.exchange.core.navigationtools

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class InterScreenRequest<T> private constructor(val id: String) {

    @Transient
    private var callback: ((InterScreenResponse<T>) -> Unit)? = null

    val isCorrupted: Boolean get() = callback == null

    constructor(id: String, callback: (InterScreenResponse<T>) -> Unit) : this(id) {
        this.callback = callback
    }

    fun respondSuccess(result: T) {
        callback?.invoke(InterScreenResponse.Success(this, result))
    }

    fun respondError(error: Throwable) {
        callback?.invoke(InterScreenResponse.Error(this, error))
    }

    fun respondCanceled() {
        callback?.invoke(InterScreenResponse.Cancelled(this))
    }

    override fun toString() = "InterScreenRequest(id=$id)"
    override fun equals(other: Any?): Boolean = other is InterScreenRequest<*> && id == other.id
    override fun hashCode() = id.hashCode()
}

sealed interface InterScreenResponse<T> {
    val request: InterScreenRequest<T>

    data class Success<T>(
        override val request: InterScreenRequest<T>,
        val result: T,
    ) : InterScreenResponse<T>

    data class Error<T>(
        override val request: InterScreenRequest<T>,
        val error: Throwable,
    ) : InterScreenResponse<T>

    data class Cancelled<T>(
        override val request: InterScreenRequest<T>,
    ) : InterScreenResponse<T>
}
