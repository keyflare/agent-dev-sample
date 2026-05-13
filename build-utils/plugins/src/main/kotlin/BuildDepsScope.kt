@file:Suppress("MemberVisibilityCanBePrivate", "unused")

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

class BuildDepsScope internal constructor(private val project: Project) {

    private val kmp: KotlinMultiplatformExtension
        get() = runCatching { project.kmp }
            .onFailure { error("KMP plugin is not applied. Is this a multiplatform module?") }
            .getOrThrow()

    fun androidDeps(configure: KotlinDependencyHandler.() -> Unit) {
        kmp.sourceSets.getByName("androidMain").dependencies { configure() }
    }

    fun iosDeps(configure: KotlinDependencyHandler.() -> Unit) {
        kmp.sourceSets.getByName("iosMain").dependencies { configure() }
    }

    fun jvmDeps(configure: KotlinDependencyHandler.() -> Unit) {
        kmp.sourceSets.getByName("jvmMain").dependencies { configure() }
    }

    fun commonDeps(configure: KotlinDependencyHandler.() -> Unit) {
        kmp.sourceSets.getByName("commonMain").dependencies { configure() }
    }

    fun commonTestDeps(configure: KotlinDependencyHandler.() -> Unit) {
        kmp.sourceSets.getByName("commonTest").dependencies { configure() }
    }
}
