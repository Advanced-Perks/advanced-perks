plugins {
    id("java")
    id("com.github.johnrengelman.shadow").version("7.1.2")
    id("checkstyle")
}

object Version {
    const val SPIGOT = "1.16.5-R0.1-SNAPSHOT"
    const val GUICE = "7.0.0"
    const val SLF4J = "2.0.9"
    const val MOCKITO = "4.0.0"
    const val JUNIT = "5.8.1"
}

group = "de.fabilucius"
version = "3.0.0"

repositories {
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    mavenCentral()
}

dependencies {
    testImplementation("org.mockito", "mockito-inline", Version.MOCKITO)
    testImplementation("org.mockito", "mockito-junit-jupiter", Version.MOCKITO)
    testImplementation("org.junit.jupiter", "junit-jupiter-api", Version.JUNIT)
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine", Version.JUNIT)

    implementation("org.spigotmc", "spigot-api", Version.SPIGOT)
    implementation("com.google.inject", "guice", Version.GUICE)
    implementation("org.slf4j", "slf4j-api", Version.SLF4J)
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