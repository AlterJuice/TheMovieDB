package data

private const val PROPS_FOLDER_NAME = "properties"


/** A simple structure for storing info about properties files */
interface PropsFile {
    val fileName: String
    val filePath: String
}

internal fun PropsFile.defaultPropertiesFilePath(): String {
    return "./${PROPS_FOLDER_NAME}/${fileName}"
}