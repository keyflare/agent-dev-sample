plugins {
    applyFor(androidLibrary = true, multiplatform = true)
}

setup {
    androidLibrary(namespace = "com.keyflare.exchange.core.analytics")
    iosLibrary(name = "Analytics")
}
