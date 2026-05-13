plugins {
    applyFor(androidLibrary = true, multiplatform = true)
    alias(libs.plugins.kotlinxSerialization)
}

deps {
    commonDeps {
        implementation(libs.coroutines)
        implementation(libs.decompose)
        implementation(libs.kotlinxSerialization)

        implementation(projects.common.utils)
        api(projects.core.navigationTools)
        implementation(projects.core.icons)
        api(projects.core.utilityScreen)
        api(projects.feature.debugPanel.api)
    }

    commonTestDeps {
        implementation(libs.kotlin.test)
        implementation(libs.coroutines.test)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.feature.debugpanel.impl")
    iosLibrary(name = "ExchangeDebugPanelImpl")
}
