sealed interface SetupTarget {

    sealed interface Android : SetupTarget {
        val namespace: String

        data class Application(
            override val namespace: String,
            val versionName: String,
            val versionCode: Int,
        ) : Android

        data class Library(
            override val namespace: String,
            val publishArtifact: PublishArtifact.Aar?
        ) : Android
    }

    sealed interface Jvm : SetupTarget {
        val group: String
        val version: String

        data class Application(
            override val group: String,
            override val version: String,
        ) : Jvm

        data class Library(
            override val group: String,
            override val version: String,
            val publishArtifact: PublishArtifact.Jar?
        ) : Jvm
    }

    sealed interface Ios : SetupTarget {

        data class Library(
            val name: String,
            val directIntegrationInSwift: Boolean,
            val publishArtifact: PublishArtifact.XCFramework?,
            val exportDependencies: List<Any>,
        ) : Ios
    }
}

sealed interface PublishArtifact {
    object XCFramework : PublishArtifact
    object Aar : PublishArtifact
    data class Jar(val version: String) : PublishArtifact
}
