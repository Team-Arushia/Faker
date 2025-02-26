plugins {
    id("java")
}

group = "io.github.bindglam"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    mavenCentral()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

    compileOnly("com.github.retrooper:packetevents-spigot:2.6.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}