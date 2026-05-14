plugins {
    applyFor(androidLibrary = true, multiplatform = true)
    alias(libs.plugins.kotlinxSerialization)
}

deps {
    commonDeps {
        implementation(libs.coroutines)
        implementation(libs.kotlinxSerialization)
        implementation(libs.ktor.clientCore)
        implementation(libs.ktor.clientContentNegotiation)
        implementation(libs.ktor.kotlinxSerializationJson)
    }

    commonTestDeps {
        implementation(libs.kotlin.test)
        implementation(libs.coroutines.test)
        implementation(libs.ktor.clientMock)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.core.comicvine")
    iosLibrary(name = "ExchangeComicVine")
}
