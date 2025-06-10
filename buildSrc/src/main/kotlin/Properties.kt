import data.PropsFile
import data.PropsKey
import data.defaultPropertiesFilePath

object MovieDBServiceProps: PropsFile {
    override val fileName: String = "MovieDBService.properties"
    override val filePath: String get() = defaultPropertiesFilePath()
    val BASE_URL = PropsKey.Single("TMDB_BASE_URL")

    val API_TOKEN = PropsKey.BuildTypeBased(
        generalKey = "TMDB_API_TOKEN",
        debug = "TMDB_DEBUG_API_TOKEN",
        release = "TMDB_RELEASE_API_TOKEN",
    )
}

