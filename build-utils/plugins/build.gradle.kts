plugins {
    `kotlin-dsl`
}

group = "com.sravni.build.utils.plugins"

dependencies {
    compileOnly(libs.androidGradlePlugin)
    compileOnly(libs.kotlinGradlePlugin)
    compileOnly(libs.composeGradlePlugin)
}

gradlePlugin {
    plugins {
        register("build-plugin") {
            id = "build-plugin"
            implementationClass = "BuildPlugin"
        }
    }
}
