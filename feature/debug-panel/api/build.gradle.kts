plugins {
    applyFor(androidLibrary = true, multiplatform = true)
    alias(libs.plugins.kotlinxSerialization)
}

deps {
    commonDeps {
        implementation(libs.coroutines)
        implementation(libs.kotlinxSerialization)
        implementation(libs.decompose)
        implementation(projects.core.navigationTools)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.feature.debugpanel.api")
    iosLibrary(name = "ExchangeDebugPanelApi")

    useExplicitApi = false
}
