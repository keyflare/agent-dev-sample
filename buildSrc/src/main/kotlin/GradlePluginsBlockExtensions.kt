import org.gradle.kotlin.dsl.PluginDependenciesSpecScope

fun PluginDependenciesSpecScope.applyFor(
    androidApplication: Boolean = false,
    androidLibrary: Boolean = false,
    jvmApplication: Boolean = false,
    jvmLibrary: Boolean = false,
    multiplatform: Boolean = false,
    compose: Boolean = false,
    publish: Boolean = false,
) {
    fun pluginBuild() = id("build-plugin")
    fun pluginAndroidApp() = id("com.android.application")
    fun pluginAndroidLib() = id("com.android.library")
    fun pluginKotlinAndroid() = id("org.jetbrains.kotlin.android")
    fun pluginKotlinJvm() = id("org.jetbrains.kotlin.jvm")
    fun pluginKotlinMultiplatform() = id("org.jetbrains.kotlin.multiplatform")
    fun pluginCompose() = id("org.jetbrains.kotlin.plugin.compose")
    fun pluginComposeMultiplatform() = id("org.jetbrains.compose")
    fun pluginMavenPublish() = id("org.gradle.maven-publish")

    if (androidApplication) {
        require(!multiplatform) {
            "You have to setup Android Application module separately from you multiplatform logic"
        }
        require(!androidLibrary) {
            "You can't setup module as an application and a library at the same time"
        }
        require(!jvmApplication && !jvmLibrary) {
            "You can't setup Android Application module with JVM target"
        }
    }

    if (jvmApplication) {
        require(!multiplatform) {
            "You have to setup JVM Application module separately from you multiplatform logic"
        }
        require(!jvmLibrary) {
            "You can't setup module as an application and a library at the same time"
        }
        require(!androidApplication && !androidLibrary) {
            "You can't setup JVM Application module with Android target"
        }
    }

    pluginBuild()

    when {
        androidApplication -> {
            pluginAndroidApp()
            pluginKotlinAndroid()
            if (compose) {
                pluginCompose()
            }
        }

        jvmApplication -> {
            pluginKotlinJvm()
            if (compose) {
                pluginCompose()
            }
        }

        androidLibrary && !multiplatform -> {
            pluginAndroidLib()
            pluginKotlinAndroid()
            if (compose) {
                pluginCompose()
            }
            if (publish) {
                pluginMavenPublish()
            }
        }

        jvmLibrary && !multiplatform -> {
            pluginKotlinJvm()
            if (compose) {
                pluginCompose()
            }
            if (publish) {
                pluginMavenPublish()
            }
        }

        multiplatform -> {
            pluginKotlinMultiplatform()
            if (compose) {
                pluginCompose()
                pluginComposeMultiplatform()
            }
            if (androidLibrary) {
                pluginAndroidLib()
            }
            if (publish) {
                pluginMavenPublish()
            }
        }
    }
}
