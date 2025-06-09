import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = ProjectConfig.javaVersion
    targetCompatibility = ProjectConfig.javaVersion
}
kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(ProjectConfig.jvmVersion)
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.dagger)
    api(libs.paging.common.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.coroutines)
    testImplementation(kotlin("test"))
}
