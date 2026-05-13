plugins {
    applyFor(androidLibrary = true, multiplatform = true)
}

deps {
    commonDeps {
        api(libs.bundles.datastore)
    }
}

setup {
    androidLibrary(namespace = "com.keyflare.common.datastore")
    iosLibrary(name = "ExchangeDataStore")

    useExplicitApi = false
}
