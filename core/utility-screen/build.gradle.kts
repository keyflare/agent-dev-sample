plugins {
    applyFor(androidLibrary = true, multiplatform = true, compose = true)
}

deps {
    commonDeps {
        implementation(compose.dependencies.ui)
        implementation(compose.dependencies.foundation)
        implementation(compose.dependencies.runtime)
        implementation(compose.dependencies.material)

        implementation(projects.core.designSystem)
        api(projects.core.icons)
    }

    commonTestDeps {
        implementation(libs.kotlin.test)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.core.utilityscreen")
    iosLibrary(name = "ExchangeUtilityScreen")

    useExplicitApi = false
}
