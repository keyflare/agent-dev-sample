plugins {
    applyFor(androidLibrary = true, multiplatform = true, compose = true)
    alias(libs.plugins.kotlinxSerialization)
}

deps {
    commonDeps {
        implementation(libs.coroutines)
        implementation(libs.kotlinxSerialization)
        implementation(libs.composeMp.runtime)

        implementation(projects.common.utils)
        implementation(projects.common.dataStore)

        implementation(projects.core.analytics)
        implementation(projects.core.navigationTools)
        implementation(projects.core.strings)
        api(projects.core.comicvine)

        api(projects.feature.mainScreen.api)
    }

    commonTestDeps {
        implementation(libs.kotlin.test)
        implementation(libs.coroutines.test)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.feature.mainscreen.impl")
    iosLibrary(name = "MainScreenImpl")
}
