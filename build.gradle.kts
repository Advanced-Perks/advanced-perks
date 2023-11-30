plugins {
    id("java")
    id("com.github.johnrengelman.shadow").version("7.1.2")
    id("checkstyle")
}

object Version {
    const val SPIGOT = "1.16.5-R0.1-SNAPSHOT"
    const val GUICE = "7.0.0"
    const val MOCKITO = "4.0.0"
    const val JUNIT = "5.8.1"
    const val JETBRAINS_ANNOTATIONS = "24.1.0"
    const val COMMONS_IO = "2.15.0"
    const val COMMONS_CODEC = "1.16.0"
    const val AUTHLIB = "1.5.25"
    const val PLACEHOLDER_API = "2.11.5"
}

group = "de.fabilucius"
version = "3.0.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://libraries.minecraft.net/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    testImplementation("org.mockito", "mockito-inline", Version.MOCKITO)
    testImplementation("org.mockito", "mockito-junit-jupiter", Version.MOCKITO)
    testImplementation("org.junit.jupiter", "junit-jupiter", Version.JUNIT)

    implementation("org.spigotmc", "spigot-api", Version.SPIGOT)
    implementation("com.google.inject", "guice", Version.GUICE)
    implementation("commons-io", "commons-io", Version.COMMONS_IO)
    implementation("commons-codec", "commons-codec", Version.COMMONS_CODEC)
    implementation("org.jetbrains", "annotations", Version.JETBRAINS_ANNOTATIONS)
    implementation("com.mojang", "authlib", Version.AUTHLIB)
    implementation("me.clip", "placeholderapi", Version.PLACEHOLDER_API)
}

tasks {
    test {
        useJUnitPlatform()
    }

    processResources {
        filesMatching("**/plugin.yml") {
            expand(project.properties)
        }
    }

    shadowJar {
        dependencies {
            include(dependency("com.google.inject:guice"))
            include(dependency("jakarta.inject:jakarta.inject-api"))
            include(dependency("aopalliance:aopalliance"))
        }
    }
}