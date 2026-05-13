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
    androidLibrary(namespace = "com.keyflare.exchange.feature.settings.api")
    iosLibrary(name = "ExchangeSettingsApi")
    useExplicitApi = false
}
