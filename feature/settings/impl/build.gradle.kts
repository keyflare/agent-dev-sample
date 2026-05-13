plugins {
    applyFor(androidLibrary = true, multiplatform = true)
    alias(libs.plugins.kotlinxSerialization)
}

deps {
    commonDeps {
        implementation(libs.coroutines)
        implementation(libs.decompose)
        implementation(libs.kotlinxSerialization)
        implementation(projects.common.dataStore)
        implementation(projects.common.utils)
        api(projects.core.navigationTools)
        implementation(projects.core.icons)
        implementation(projects.core.platform)
        api(projects.core.utilityScreen)
        api(projects.feature.settings.api)
    }

    commonTestDeps {
        implementation(libs.kotlin.test)
        implementation(libs.coroutines.test)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.feature.settings.impl")
    iosLibrary(name = "ExchangeSettingsImpl")
}

kotlin {
    sourceSets {
        androidUnitTest.dependencies {
            implementation(libs.kotlin.testJunit)
        }
    }
}
