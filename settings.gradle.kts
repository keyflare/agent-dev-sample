@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("build-utils")
rootProject.name = "exchange"

include("app:android")
include("app:shared")
include("common:data-store")
include("common:utils")
include("core:analytics")
include("core:comicvine")
include("core:design-system")
include("core:icons")
include("core:platform")
include("core:navigation-tools")
include("core:strings")
include("core:utility-screen")
include("feature:debug-panel:api")
include("feature:debug-panel:impl")
include("feature:debug-panel:ui")
include("feature:main-screen:api")
include("feature:main-screen:impl")
include("feature:main-screen:ui")
include("feature:settings:api")
include("feature:settings:impl")
include("feature:settings:ui")
