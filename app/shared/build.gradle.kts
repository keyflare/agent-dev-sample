plugins {
    applyFor(
        androidLibrary = true,
        multiplatform = true,
        // Compose plugin needed in shared app module to make
        // compose resources work in iOS app.
        compose = true
    )
    alias(libs.plugins.kotlinxSerialization)
}

deps {
    commonDeps {
        implementation(libs.coroutines)
        implementation(libs.kotlinxSerialization)
        implementation(libs.ktor.clientCore)
        implementation(libs.ktor.clientContentNegotiation)
        implementation(libs.ktor.kotlinxSerializationJson)
        api(libs.decompose)

        implementation(projects.common.dataStore)
        implementation(projects.common.utils)
        api(projects.core.navigationTools)
        api(projects.core.icons)
        api(projects.core.utilityScreen)
        api(projects.core.analytics)
        api(projects.core.platform)
        api(projects.core.comicvine)

        ////////// Features ///////////////////////////////////
        api(projects.feature.mainScreen.impl)
        api(projects.feature.debugPanel.impl)
        api(projects.feature.settings.impl)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange")
    iosLibrary(
        name = "ExchangeShared",
        directIntegrationInSwift = true,
        exportDependencies = listOf(
            libs.decompose,
            libs.essentyLifecycle,
            projects.core.navigationTools,
            projects.core.icons,
            projects.core.utilityScreen,
            projects.core.analytics,
            projects.core.platform,
            projects.core.comicvine,
            // Features
            projects.feature.mainScreen.impl,
            projects.feature.debugPanel.impl,
            projects.feature.settings.impl,
        )
    )
}

android {
    buildTypes {
        create("dev") {
            initWith(getByName("debug"))
            matchingFallbacks += listOf("debug")
        }
    }
}

deps {
    androidDeps {
        implementation(libs.ktor.clientOkHttp)
    }
    iosDeps {
        implementation(libs.ktor.clientDarwin)
    }
}

dependencies {
    add("debugImplementation", libs.chucker)
    add("devImplementation", libs.chucker)
    add("releaseImplementation", libs.chuckerNoOp)
}
