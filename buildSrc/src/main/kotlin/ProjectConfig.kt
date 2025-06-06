import org.gradle.api.JavaVersion


object ProjectConfig {
    const val compileSdk = 35
    const val minSdk = 26
    const val targetSdk = 35
    const val versionCode = 1
    const val versionName = "1.0"

    const val jvmVersion = "21"
    val javaVersion = JavaVersion.VERSION_21
}