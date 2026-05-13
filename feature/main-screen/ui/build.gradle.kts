plugins {
    applyFor(androidLibrary = true, multiplatform = true, compose = true)
}

deps {
    commonDeps {
        implementation(libs.composeMp.ui)
        implementation(libs.composeMp.foundation)
        implementation(libs.composeMp.runtime)
        implementation(libs.composeMp.material)

        implementation(projects.core.strings)
        implementation(projects.core.designSystem)
        implementation(projects.core.icons)
        api(projects.feature.mainScreen.impl)
    }

    commonTestDeps {
        implementation(libs.kotlin.test)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.feature.mainscreen.ui")
    iosLibrary(name = "MainScreenUi")
}

project.afterEvaluate {
    compose.resources {
        generateResClass = always
    }
}
