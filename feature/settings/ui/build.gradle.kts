plugins {
    applyFor(androidLibrary = true, multiplatform = true, compose = true)
}

deps {
    commonDeps {
        implementation(libs.composeMp.ui)
        implementation(libs.composeMp.foundation)
        implementation(libs.composeMp.runtime)
        implementation(projects.core.utilityScreen)
        api(projects.feature.settings.impl)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.feature.settings.ui")
    iosLibrary(name = "ExchangeSettingsUi")
}
