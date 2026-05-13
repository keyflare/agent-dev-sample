plugins {
    applyFor(androidLibrary = true, multiplatform = true, compose = true)
}

deps {
    commonDeps {
        implementation(libs.composeMp.ui)
        implementation(libs.composeMp.foundation)
        implementation(libs.composeMp.material)
        implementation(libs.composeMp.runtime)
        implementation(projects.core.designSystem)
        implementation(projects.core.utilityScreen)
        api(projects.feature.debugPanel.impl)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.feature.debugpanel.ui")
    iosLibrary(name = "ExchangeDebugPanelUi")
}
