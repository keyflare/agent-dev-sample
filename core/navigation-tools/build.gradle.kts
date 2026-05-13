plugins {
    applyFor(androidLibrary = true, multiplatform = true)
    alias(libs.plugins.kotlinxSerialization)
}

deps {
    commonDeps {
        api(libs.decompose)
        api(libs.coroutines)
        implementation(libs.kotlinxSerialization)
        implementation(projects.common.utils)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.core.navigationtools")
    iosLibrary(name = "NavigationTools")

    useExplicitApi = false
}
