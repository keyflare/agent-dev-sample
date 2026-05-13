plugins {
    applyFor(androidLibrary = true, multiplatform = true)
}

deps {
    commonDeps {
        implementation(libs.coroutines)
    }

    commonTestDeps {
        implementation(libs.kotlin.test)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.common.utils")
    iosLibrary(name = "Utils")

    useExplicitApi = false
}
