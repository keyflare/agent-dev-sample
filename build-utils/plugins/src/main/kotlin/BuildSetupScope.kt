@file:Suppress("MemberVisibilityCanBePrivate")

import com.android.build.api.dsl.ApplicationDefaultConfig
import com.android.build.api.dsl.LibraryDefaultConfig
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.kotlinExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFrameworkConfig
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class BuildSetupScope internal constructor(private val project: Project) {
    private var setupTargets: MutableSet<SetupTarget> = mutableSetOf()

    var useExplicitApi: Boolean = false

    fun androidApplication(
        namespace: String,
        versionName: String,
        versionCode: Int,
    ) {
        require(!setupTargets.any { it is SetupTarget.Android }) {
            "You already have an Android target setup. " +
                    "You can't have more than one Android target in a module."
        }
        require(project.plugins.hasPlugin("com.android.application")) {
            "You need to apply Android application plugin first (com.android.application)"
        }
        useExplicitApi = false
        setupTargets.add(
            SetupTarget.Android.Application(
                namespace = namespace,
                versionName = versionName,
                versionCode = versionCode,
            )
        )
    }

    fun androidLibrary(
        namespace: String,
        publishArtifact: PublishArtifact.Aar? = null,
    ) {
        require(!setupTargets.any { it is SetupTarget.Android }) {
            "You already have an Android target setup. " +
                    "You can't have more than one Android target in a module."
        }
        require(project.plugins.hasPlugin("com.android.library")) {
            "You need to apply Android library plugin first (com.android.library)"
        }
        require(
            project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") ||
                    project.plugins.hasPlugin("org.jetbrains.kotlin.android")
        ) {
            "You need to apply some kotlin plugin first " +
                    "(ether org.jetbrains.kotlin.multiplatform or org.jetbrains.kotlin.android)"
        }
        useExplicitApi = true
        setupTargets.add(SetupTarget.Android.Library(namespace, publishArtifact))
    }

    fun jvmApplication(
        group: String,
        version: String,
    ) {
        require(!setupTargets.any { it is SetupTarget.Jvm }) {
            "You already have a JVM target setup. " +
                    "You can't have more than one JVM target in a module."
        }
        require(project.plugins.hasPlugin("org.jetbrains.kotlin.jvm")) {
            "You need to apply JVM plugin first (org.jetbrains.kotlin.jvm)"
        }
        useExplicitApi = false
        setupTargets.add(
            SetupTarget.Jvm.Application(
                group = group,
                version = version,
            )
        )
    }

    fun jvmLibrary(
        group: String,
        publishArtifact: PublishArtifact.Jar? = null,
    ) {
        require(!setupTargets.any { it is SetupTarget.Jvm }) {
            "You already have a JVM target setup. " +
                    "You can't have more than one JVM target in a module."
        }
        require(
            project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform") ||
                    project.plugins.hasPlugin("org.jetbrains.kotlin.jvm")
        ) {
            "You need to apply some kotlin plugin first " +
                    "(ether org.jetbrains.kotlin.multiplatform or org.jetbrains.kotlin.jvm)"
        }
        useExplicitApi = true
        setupTargets.add(
            SetupTarget.Jvm.Library(
                group = group,
                version = publishArtifact?.version ?: "unspecified",
                publishArtifact = publishArtifact,
            )
        )
    }

    fun iosLibrary(
        name: String,
        directIntegrationInSwift: Boolean = false,
        publishArtifact: PublishArtifact.XCFramework? = null,
        exportDependencies: List<Any> = emptyList(),
    ) {
        require(!setupTargets.any { it is SetupTarget.Ios }) {
            "You already have an iOS target setup. " +
                    "You can't have more than one iOS target in a module."
        }
        require(project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
            "You need to apply KMP plugin first (org.jetbrains.kotlin.multiplatform)."
        }
        if (exportDependencies.isNotEmpty()) {
            require(directIntegrationInSwift || publishArtifact != null) {
                "You can't export dependencies without making the library accessible from Swift " +
                        "or without publishing the artifact."
            }
        }

        useExplicitApi = true
        setupTargets.add(
            SetupTarget.Ios.Library(
                name = name,
                directIntegrationInSwift = directIntegrationInSwift,
                publishArtifact = publishArtifact,
                exportDependencies = exportDependencies,
            )
        )
    }

    internal fun finishSetup() {
        val useCompose = verifyComposeUsageSetup()
        val androidTarget = setupTargets.filterIsInstance<SetupTarget.Android>().firstOrNull()
        val jvmTarget = setupTargets.filterIsInstance<SetupTarget.Jvm>().firstOrNull()

        setupKotlin(setupTargets, useExplicitApi = useExplicitApi, useCompose = useCompose)
        androidTarget?.let { setupAndroid(it, useCompose) }
        jvmTarget?.let { setupJvm(it, useCompose) }
    }

    fun setupMavenPublishing(group: String, version: Provider<String?>) {
        require(project.plugins.hasPlugin("org.gradle.maven-publish")) {
            "You need to apply maven-publish plugin first (org.gradle.maven-publish)"
        }
        require(version.isPresent) {
            "You need to provide version for maven publication"
        }

        project.group = group
        project.version = version.get()

        project.publishing {
            repositories {
                maven {
                    name = "GitHubPackages"
                    url = project.uri("https://maven.pkg.github.com/sravni/constructor")
                    credentials {
                        // If you want to publish manually,
                        // please provide credentials in local.properties file or in environment variables
                        username = project.getLocalProperty("github.username")
                            ?: System.getenv("GITHUB_ACTOR")
                        password = project.getLocalProperty("github.token")
                            ?: System.getenv("GITHUB_TOKEN")
                    }
                }
            }
        }
    }

    private fun verifyComposeUsageSetup(): Boolean {
        val androidComposePluginApplied =
            project.plugins.hasPlugin("org.jetbrains.kotlin.plugin.compose")
        val kmpComposePluginApplied =
            project.plugins.hasPlugin("org.jetbrains.compose")

        val hasAndroidTarget = setupTargets.any { it is SetupTarget.Android }
        val hasOtherTargets = setupTargets.any { it !is SetupTarget.Android }

        when {
            // Pure Android library
            hasAndroidTarget && !hasOtherTargets -> {
                require(!kmpComposePluginApplied) {
                    "For pure android library you don't need to apply KMP Compose plugin " +
                            "(org.jetbrains.compose). You need to apply Android Compose plugin " +
                            "(org.jetbrains.kotlin.plugin.compose)"
                }
            }
            // Library without android target at all
            !hasAndroidTarget -> {
                require(!androidComposePluginApplied) {
                    "You have a module without Android target. " +
                            "You don't need to apply Android Compose plugin " +
                            "(org.jetbrains.kotlin.plugin.compose). "
                }
            }
            // KMP module with Android target and Compose enabled
            hasAndroidTarget && (androidComposePluginApplied || kmpComposePluginApplied) -> {
                require(androidComposePluginApplied) {
                    "You have an Android target in your KMP module. " +
                            "You need to apply Android Compose plugin " +
                            "(org.jetbrains.kotlin.plugin.compose) to support previews and all " +
                            "the additional features of Android Compose."
                }
            }
        }

        return androidComposePluginApplied || kmpComposePluginApplied
    }

    private fun setupKotlin(
        targets: Set<SetupTarget>,
        useExplicitApi: Boolean,
        useCompose: Boolean,
    ) {
        if (useExplicitApi) {
            project.kotlinExtension.explicitApi = ExplicitApiMode.Strict
        }

        project.tasks.withType<KotlinCompile>().configureEach {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_21)
            }
        }

        when {
            targets.isEmpty() -> error("You are trying to setup a module without any target")

            // When we have one target, we have a chance that we don't need to enable KMP.
            // Some targets are supported by kotlin natively without KMP - JVM, Android.
            targets.size == 1 -> {
                when (targets.first()) {
                    is SetupTarget.Android -> setupKotlinForAndroid()
                    is SetupTarget.Jvm -> setupKotlinForJvm()

                    // All the other platforms are considered as multiplatform
                    else -> setupKotlinForMultiplatform(targets, useCompose)
                }
            }

            else -> setupKotlinForMultiplatform(targets, useCompose)
        }
    }

    private fun setupKotlinForAndroid() {
        // Nothing here for now
    }

    private fun setupKotlinForJvm() {
        // Nothing here for now
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    private fun setupKotlinForMultiplatform(
        targets: Set<SetupTarget>,
        useCompose: Boolean,
    ) {
        project.kmp.apply {
            applyDefaultHierarchyTemplate()

            targets.forEach { target ->
                when (target) {
                    is SetupTarget.Android.Library -> setupAndroidTarget()
                    is SetupTarget.Ios.Library -> this.setupIosTarget(target)
                    is SetupTarget.Jvm.Library -> this.setupJvmTarget(target)
                    else -> error("Unsupported target for multiplatform setup: $target")
                }
            }

            sourceSets {
                val commonMain by getting {
                    // List of default dependencies which will be included in every KMP module
                    dependencies {
                        implementation(project.getLib("kermit"))
                        if (useCompose) {
                            implementation(project.getLib("composeMp.componentsResources"))
                            implementation(project.getLib("composeMp.uiToolingPreview"))
                        }
                    }
                }
            }

            if (useCompose) {
                project.compose {
                    resources {
                        generateResClass = ResourcesExtension.ResourceClassGeneration.Never
                    }
                }
            }

            this.targets.all {
                compilations.all {
                    compileTaskProvider.configure {
                        compilerOptions {
                            freeCompilerArgs.add("-Xexpect-actual-classes")
                        }
                    }
                }
            }
        }
    }

    private fun KotlinMultiplatformExtension.setupAndroidTarget() {
        androidTarget {
            publishLibraryVariants("debug", "release")
        }
        sourceSets {
            // List of default dependencies which will be included in
            // every KMP module in an Android source set
            val androidMain by getting {
                // Nothing here for now
            }
        }
    }

    private fun KotlinMultiplatformExtension.setupIosTarget(target: SetupTarget.Ios.Library) {
        val xcf = target.publishArtifact?.let { XCFrameworkConfig(project, target.name) }

        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64(),
        ).forEach {
            it.binaries.framework {
                target.exportDependencies.forEach { dependency ->
                    export(dependency)
                }
                xcf?.let { safeXcf ->
                    binaryOption(
                        name = "bundleId",
                        value = "com.sravni.bdui.${target.name}",
                    )
                    safeXcf.add(this)
                }

                this.baseName = target.name
                this.isStatic = target.directIntegrationInSwift || target.publishArtifact != null
            }
        }

        sourceSets {
            // List of default dependencies which will be included in
            // every KMP module in an iOS source set
            val iosMain by getting {
                // Nothing here for now
            }
        }
    }

    private fun KotlinMultiplatformExtension.setupJvmTarget(@Suppress("UNUSED_PARAMETER") target: SetupTarget.Jvm.Library) {
        jvm()
        sourceSets {
            // List of default dependencies which will be included in
            // every KMP module in an JVM source set
            val jvmMain by getting {
                // Nothing here for now
            }
        }
    }

    private fun setupAndroid(
        androidTarget: SetupTarget.Android,
        useCompose: Boolean,
    ) {
        project.android.apply {
            this.namespace = androidTarget.namespace
            compileSdk = project.compileSdkVersion

            defaultConfig {
                minSdk = project.minSdkVersion

                when (androidTarget) {
                    is SetupTarget.Android.Application -> {
                        with(this@defaultConfig as ApplicationDefaultConfig) {
                            targetSdk = project.targetSdkVersion
                        }
                    }

                    is SetupTarget.Android.Library -> {
                        with((this@defaultConfig as LibraryDefaultConfig)) {
                            consumerProguardFiles("consumer-rules.pro")
                        }
                    }
                }
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_21
                targetCompatibility = JavaVersion.VERSION_21
            }

            if (useCompose) {
                buildFeatures {
                    compose = true
                }
            }
        }

        if (androidTarget is SetupTarget.Android.Application) {
            project.androidApp.defaultConfig {
                versionName = androidTarget.versionName
                versionCode = androidTarget.versionCode
            }
        }

        if (useCompose) {
            val isMultiplatformModule = project.plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")

            project.dependencies {
                if (isMultiplatformModule) {
                    add("debugImplementation", project.getLib("composeMp.uiTooling"))
                } else {
                    add("debugApi", project.getLib("composeAndroid.uiTooling"))
                    add("api", project.getLib("composeAndroid.uiToolingPreview"))
                }
            }
        }
    }

    private fun setupJvm(
        jvmTarget: SetupTarget.Jvm,
        @Suppress("UNUSED_PARAMETER") useCompose: Boolean,
    ) {
        project.group = jvmTarget.group
        project.version = jvmTarget.version
    }
}
