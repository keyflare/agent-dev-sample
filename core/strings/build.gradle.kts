plugins {
    applyFor(androidLibrary = true, multiplatform = true, compose = true)
}

deps {
    commonDeps {
        implementation(libs.coroutines)
        api(libs.composeMp.componentsResources)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.core.strings")
    iosLibrary(name = "ExchangeStrings")

    useExplicitApi = false
}

project.afterEvaluate {
    compose.resources {
        generateResClass = always
        publicResClass = true
    }
}
