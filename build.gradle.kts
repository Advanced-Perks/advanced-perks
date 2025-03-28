plugins {
    id("java")
    id("com.github.johnrengelman.shadow").version("7.1.2")
    id("checkstyle")
    id("name.remal.sonarlint").version("3.3.17")
    kotlin("jvm")
}

object Version {
    const val SPIGOT = "1.20.4-R0.1-SNAPSHOT"
    const val GUICE = "7.0.0"
    const val MOCKITO = "4.0.0"
    const val JUNIT = "5.8.1"
    const val JETBRAINS_ANNOTATIONS = "24.1.0"
    const val COMMONS_IO = "2.15.0"
    const val COMMONS_CODEC = "1.16.0"
    const val AUTHLIB = "1.5.25"
    const val PLACEHOLDER_API = "2.11.5"
    const val VAULT = "1.7.1"
    const val LUCK_PERMS = "5.4"
    const val X_SERIES = "9.3.0"
    const val ARCHUNIT = "1.2.1"
    const val DATAFAKER = "2.0.2"
}

group = "de.fabilucius"
version = "3.4.1"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://libraries.minecraft.net/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    testImplementation("org.mockito", "mockito-inline", Version.MOCKITO)
    testImplementation("org.mockito", "mockito-junit-jupiter", Version.MOCKITO)
    testImplementation("org.junit.jupiter", "junit-jupiter", Version.JUNIT)
    testImplementation("com.tngtech.archunit", "archunit", Version.ARCHUNIT)
    testImplementation("net.datafaker", "datafaker", Version.DATAFAKER) {
        //To resolve classpath dependency problems because spigot uses an older version which gets overwritten by datafaker
        exclude(group = "org.yaml", module = "snakeyaml")
    }
    testImplementation("io.kotest", "kotest-assertions-core-jvm", "5.9.1")

    implementation("org.spigotmc", "spigot-api", Version.SPIGOT)
    implementation("com.google.inject", "guice", Version.GUICE)
    implementation("com.google.inject.extensions", "guice-assistedinject", Version.GUICE)
    implementation("commons-io", "commons-io", Version.COMMONS_IO)
    implementation("commons-codec", "commons-codec", Version.COMMONS_CODEC)
    implementation("org.jetbrains", "annotations", Version.JETBRAINS_ANNOTATIONS)
    implementation("com.mojang", "authlib", Version.AUTHLIB)
    implementation("me.clip", "placeholderapi", Version.PLACEHOLDER_API)
    implementation("com.github.MilkBowl", "VaultAPI", Version.VAULT)
    implementation("net.luckperms", "api", Version.LUCK_PERMS)
    implementation("com.github.cryptomorin", "XSeries", Version.X_SERIES)
    implementation(kotlin("stdlib-jdk8"))
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

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        dependencies {
            include(dependency("com.google.inject:guice"))
            include(dependency("com.google.inject.extensions:guice-assistedinject"))
            include(dependency("jakarta.inject:jakarta.inject-api"))
            include(dependency("aopalliance:aopalliance"))
            include(dependency("com.github.cryptomorin:XSeries"))
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        }
        val externalPackage = "de.fabilucius.external"
        relocate("com.cryptomorin.xseries", "$externalPackage.com.cryptomorin.xseries")
        relocate("com.google.inject", "$externalPackage.com.google.inject")
        relocate("jakarta.inject", "$externalPackage.jakarta.inject")
        relocate("org.aopalliance", "$externalPackage.org.aopalliance")
        archiveFileName.set("${project.name}-$version.jar")
    }

    sonarLint {
        rules {
            disable(
                "java:S3010",
                "java:S1192", //TODO enable that rule again
                "java:S1168",
                "java:S110",
                "java:S3655",
                "java:S1141",
                "java:S3011",
                "java:S6355",
                "java:S1123",
                "java:S2629",
                "java:S2142",
                "java:S4144",
                "java:S107", //activate in the future (too many parameter in constructor)
                "java:S3358",
                "java:S899",
                "java:S1135",
                "java:S1133",
                "java:S5778",
                "java:S1068"
            )
        }
        val ignoreFiles = listOf("**Metrics.java", "**InventoryUpdate.java", "**ReflectionUtils.java")
        ignoredPaths.addAll(ignoreFiles)
    }

}