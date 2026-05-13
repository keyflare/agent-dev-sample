import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildPlugin : Plugin<Project> {
    override fun apply(target: Project) = Unit
}

fun Project.setup(block: BuildSetupScope.() -> Unit) {
    BuildSetupScope(project = this).apply {
        block()
        finishSetup()
    }
}

fun Project.deps(block: BuildDepsScope.() -> Unit) {
    BuildDepsScope(project = this).apply {
        block()
    }
}
