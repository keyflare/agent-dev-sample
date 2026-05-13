import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.konan.properties.loadProperties

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.getVersion(name: String): String =
    libs.findVersion(name).get().toString()

internal fun Project.getLib(name: String): Provider<MinimalExternalModuleDependency> =
    libs.findLibrary(name).get()

internal val Project.publishing: PublishingExtension
    get() = (this as ExtensionAware).extensions.getByName("publishing") as PublishingExtension

internal fun Project.publishing(configure: Action<PublishingExtension>): Unit =
    (this as ExtensionAware).extensions.configure("publishing", configure)

internal val Project.android: CommonExtension<*, *, *, *, *, *>
    get() = extensions.getByType(CommonExtension::class.java)

internal val Project.androidApp: BaseAppModuleExtension
    get() = extensions.getByType(BaseAppModuleExtension::class.java)

internal val Project.kmp: KotlinMultiplatformExtension
    get() = extensions.getByType(KotlinMultiplatformExtension::class.java)

internal fun Project.compose(configure: Action<ComposeExtension>): Unit =
    (this as ExtensionAware).extensions.configure("compose", configure)

internal fun ComposeExtension.resources(configure: Action<ResourcesExtension>): Unit =
    (this as ExtensionAware).extensions.configure("resources", configure)

internal fun Project.getLocalProperty(key: String): String? {
    val properties = file(rootDir.path + "/local.properties")
    return properties
        .takeIf { it.exists() }
        ?.let { loadProperties(properties.path).getProperty(key) }
}

internal val Project.minSdkVersion: Int get() = getVersion("minSdk").toInt()
internal val Project.targetSdkVersion: Int get() = getVersion("targetSdk").toInt()
internal val Project.compileSdkVersion: Int get() = getVersion("compileSdk").toInt()
