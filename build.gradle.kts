plugins {
    java
    `maven-publish`
}

fun prop(name: String): String =
    providers.gradleProperty(name).get()

val projectName = prop("project_name")
val projectGroup = prop("project_group")
val projectVersion = prop("project_version")
val projectLicense = prop("project_license")

group = projectGroup
version = projectVersion

base {
    archivesName.set(projectName)
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(libs.javaAnnotations)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
}

java {
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = projectName
            from(components["java"])
        }
    }
}

tasks.withType<org.gradle.jvm.tasks.Jar>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from("LICENSE") {
        rename { "${it}_${archiveBaseName.get()}" }
    }
}

tasks.test {
    useJUnitPlatform()
}