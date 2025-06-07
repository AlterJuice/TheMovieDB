package data

enum class BuildTypes(
    val typeName: String,
) {
    DEBUG(typeName = "debug"),
    RELEASE(typeName = "release");
}

interface BuildTypeStruct<T> {
    val debug: T
    val release: T

    fun byBuild(type: BuildTypes) = when(type) {
        BuildTypes.DEBUG -> debug
        BuildTypes.RELEASE -> release
    }

    val entries get() = listOf(debug, release)
}
