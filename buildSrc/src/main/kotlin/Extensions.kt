import exceptions.NoSuchPropertyFieldException
import exceptions.NoSuchPropertyFileException
import org.gradle.api.Project
import java.io.FileInputStream
import java.util.Properties
import kotlin.jvm.Throws
import data.PropsKey
import data.PropsFile


@Throws(NoSuchPropertyFieldException::class)
fun Properties.getPropValue(
    propKey: PropsKey,
    buildType: String? = null,
    onFieldExists: (propKey: PropsKey, value: String) -> Unit
) {
    val key = propKey.getKey(buildType = buildType)
    if (this.containsKey(key)){
        onFieldExists.invoke(propKey, this[key]!!.toString())
    } else {
        throw NoSuchPropertyFieldException(key)
    }
}

@Throws(NoSuchPropertyFileException::class)
fun Project.loadProperties(
    propsFileStructure: PropsFile
): Properties {
    val props = Properties()
    val propsFile = project.rootProject.file(propsFileStructure.filePath)
    if (!propsFile.exists()){
        throw NoSuchPropertyFileException(propsFile.name, propsFile.path)
    }
    props.load(FileInputStream(propsFile))
    return props
}
