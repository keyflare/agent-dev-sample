plugins {
    applyFor(androidLibrary = true, multiplatform = true, compose = true)
}

deps {
    commonDeps {
        implementation(libs.coroutines)
        implementation(libs.composeMp.runtime)
        implementation(projects.core.analytics)
    }

    commonTestDeps {
        implementation(libs.kotlin.test)
        implementation(libs.coroutines.test)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.core.platform")
    iosLibrary(name = "ExchangePlatform")

    useExplicitApi = false
}
