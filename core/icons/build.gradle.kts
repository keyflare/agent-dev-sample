plugins {
    applyFor(androidLibrary = true, multiplatform = true, compose = true)
}

deps {
    commonDeps {
        implementation(libs.composeMp.ui)
    }

    commonTestDeps {
        implementation(libs.kotlin.test)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.core.icons")
    iosLibrary(name = "ExchangeIcons")

    useExplicitApi = false
}
