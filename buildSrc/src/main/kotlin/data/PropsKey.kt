package data


sealed interface PropsKey {
    val generalKey: String

    class Single(override val generalKey: String): PropsKey

    class BuildTypeBased(
        override val generalKey: String,
        override val debug: String,
        override val release: String
    ): PropsKey, BuildTypeStruct<String>


    fun getKey(
        buildType: String? = null
    ): String = when(this) {
        is Single -> generalKey
        is BuildTypeBased -> this.byBuild(unpackBuildType(buildType))
    }

    private fun unpackBuildType(buildType: String?): BuildTypes {
        buildType?: throw Exception("Build type is required for BuildTypeBased Property but received 'null'")
        return BuildTypes.values().find {
            it.typeName == buildType
        }?: throw IllegalStateException("Not supported build type: Got $buildType")
    }
}


