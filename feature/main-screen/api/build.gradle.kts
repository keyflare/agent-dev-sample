plugins {
    applyFor(androidLibrary = true, multiplatform = true)
    alias(libs.plugins.kotlinxSerialization)
}

deps {
    commonDeps {
        implementation(libs.coroutines)
        implementation(libs.kotlinxSerialization)

        api(projects.core.navigationTools)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.feature.mainscreen.api")
    iosLibrary(name = "MainScreenApi")

    useExplicitApi = false
}
