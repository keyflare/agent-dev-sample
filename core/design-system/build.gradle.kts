plugins {
    applyFor(androidLibrary = true, multiplatform = true, compose = true)
}

deps {
    commonDeps {
        implementation(libs.composeMp.ui)
        implementation(libs.composeMp.runtime)
        implementation(libs.composeMp.material)
        implementation(libs.composeMp.foundation)

        api (projects.core.icons)
        api (projects.core.strings)
    }
    commonTestDeps {
        implementation(libs.kotlin.test)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.core.ds")
    iosLibrary(name = "ExchangeDs")

    useExplicitApi = false
}

project.afterEvaluate {
    compose.resources {
        generateResClass = always
        publicResClass = true
    }
}
