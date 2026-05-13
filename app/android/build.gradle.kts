plugins {
    applyFor(androidApplication = true, compose = true)
}

setup {
    androidApplication(
        namespace = "com.keyflare.exchange.android",
        versionName = "0.0.0",
        versionCode = 1,
    )
}

fun mobileVersion(command: String, release: Boolean): Provider<String> =
    providers.exec {
        commandLine(
            rootProject.layout.projectDirectory.file("scripts/mobile-version").asFile.absolutePath,
            command,
            if (release) "--release" else "--debug",
        )
    }.standardOutput.asText.map { it.trim() }

androidComponents {
    onVariants { variant ->
        val release = variant.buildType == "release"

        variant.outputs.forEach { output ->
            output.versionName.set(mobileVersion("version-name", release))
            output.versionCode.set(mobileVersion("build-number", release).map { it.toInt() })
        }
    }
}

android {
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    // Libraries
    implementation(libs.androidxActivity)
    implementation(libs.decomposeCompose)
    implementation(libs.appMetricaAndroid)
    testImplementation(libs.kotlin.test)

    // Compose
    implementation(libs.composeAndroid.ui)
    implementation(libs.composeAndroid.foundation)
    implementation(libs.composeAndroid.material)
    implementation(libs.composeAndroid.runtime)
    debugImplementation(libs.composeAndroid.uiTooling)
    api(libs.composeAndroid.uiToolingPreview)

    // Project Modules
    implementation(projects.app.shared)
    implementation(projects.common.dataStore)
    implementation(projects.common.utils)
    implementation(projects.core.analytics)
    implementation(projects.core.designSystem)
    implementation(projects.core.navigationTools)
    implementation(projects.core.platform)

    // Features
    implementation(projects.feature.mainScreen.ui)
    implementation(projects.feature.debugPanel.ui)
    implementation(projects.feature.settings.ui)
}

val releaseSigningEnvNames = listOf(
    "ANDROID_RELEASE_KEYSTORE_FILE",
    "ANDROID_RELEASE_KEYSTORE_PASSWORD",
    "ANDROID_RELEASE_KEY_ALIAS",
    "ANDROID_RELEASE_KEY_PASSWORD",
)
val releaseRequiredEnvNames = releaseSigningEnvNames + listOf(
    "EXCHANGE_APPMETRICA_API_KEY",
)

val missingReleaseSigningEnvNames = releaseSigningEnvNames.filter { name ->
    System.getenv(name).isNullOrBlank()
}
val hasReleaseSigning = missingReleaseSigningEnvNames.isEmpty()
val missingReleaseRequiredEnvNames = releaseRequiredEnvNames.filter { name ->
    System.getenv(name).isNullOrBlank()
}

fun isReleaseSigningRequiredTask(taskName: String): Boolean {
    val normalizedTaskName = taskName.lowercase()
    return "release" in normalizedTaskName && listOf(
        "assemble",
        "build",
        "bundle",
        "package",
        "sign",
    ).any { it in normalizedTaskName }
}

gradle.taskGraph.whenReady {
    val releaseSigningRequired = allTasks.any { task ->
        task.project.path == project.path && isReleaseSigningRequiredTask(task.name)
    }

    if (releaseSigningRequired && missingReleaseRequiredEnvNames.isNotEmpty()) {
        error(
            "Missing Android release environment variables: " +
                    missingReleaseRequiredEnvNames.joinToString()
        )
    }
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("../../debug.keystore")
            storePassword = "android"
            keyAlias = "debug"
            keyPassword = "android"
        }

        create("release") {
            if (hasReleaseSigning) {
                storeFile = file(System.getenv("ANDROID_RELEASE_KEYSTORE_FILE"))
                storePassword = System.getenv("ANDROID_RELEASE_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("ANDROID_RELEASE_KEY_ALIAS")
                keyPassword = System.getenv("ANDROID_RELEASE_KEY_PASSWORD")
            }
        }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs["debug"]
            applicationIdSuffix = ".debug"
            buildConfigField(
                "String",
                "APPMETRICA_API_KEY",
                "\"${System.getenv("EXCHANGE_APPMETRICA_API_KEY_DEV").orEmpty()}\"",
            )
            manifestPlaceholders.putAll(
                mapOf(
                    "ALLOW_HTTP" to true,
//                    "APP_LABEL" to "Debug",
//                    "APP_ICON" to "@mipmap/ic_launcher_debug_round",
                )
            )
        }

        release {
            signingConfig = if (hasReleaseSigning) {
                signingConfigs["release"]
            } else {
                signingConfigs["debug"]
            }
            buildConfigField(
                "String",
                "APPMETRICA_API_KEY",
                "\"${System.getenv("EXCHANGE_APPMETRICA_API_KEY").orEmpty()}\"",
            )
            manifestPlaceholders.putAll(
                mapOf(
                    "ALLOW_HTTP" to false,
//                    "APP_LABEL" to "@string/app_name",
//                    "APP_ICON" to "@mipmap/ic_launcher_round",
                )
            )
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        create("dev") {
            initWith(getByName("release"))
            signingConfig = signingConfigs["debug"]
            matchingFallbacks += listOf("debug", "release")
            applicationIdSuffix = ".dev"
            buildConfigField(
                "String",
                "APPMETRICA_API_KEY",
                "\"${System.getenv("EXCHANGE_APPMETRICA_API_KEY_DEV").orEmpty()}\"",
            )
            manifestPlaceholders.putAll(
                mapOf(
                    "ALLOW_HTTP" to true,
//                    "APP_LABEL" to "Dev",
//                    "APP_ICON" to "@mipmap/ic_launcher1_dev_round",
                )
            )
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }
}
