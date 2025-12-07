plugins {
    id("java")
    application
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "me.koply"
version = "1.0-SNAPSHOT"
description = "Pikap"

application {
    mainClass = "me.koply.pikap.Main"
    applicationDefaultJvmArgs = listOf("-Dfile.encoding=UTF-8")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

repositories {
    mavenCentral()
    maven(url = "https://maven.lavalink.dev/releases/")
    maven(url = "https://jitpack.io/")
}

dependencies {
    // LAVAPLAYER
    implementation(libs.dev.arbjerg.lavaplayer)
    implementation(libs.dev.lavalink.youtube.v2)

    // SQL
    implementation(libs.org.xerial.sqlite.jdbc)
    implementation(libs.com.j256.ormlite.ormlite.core)
    implementation(libs.com.j256.ormlite.ormlite.jdbc)

    // LOGGING
    implementation(libs.org.slf4j.slf4j.api)
    implementation(libs.ch.qos.logback.logback.classic)

    // LOMBOK
    compileOnly(libs.org.projectlombok.lombok)
    annotationProcessor(libs.org.projectlombok.lombok)

    // TERMINAL
    implementation(libs.com.github.tomas.langer.chalk)

    // MISC
    implementation(libs.com.github.kwhat.jnativehook)
    implementation(libs.com.github.jncrmx.discord.game.sdk4j)

    // DAGGER
    implementation(libs.com.google.dagger.dagger)
    annotationProcessor(libs.com.google.dagger.compiler)

    // JACKSON
    implementation(libs.com.fasterxml.jackson.databind.jackson.databind)
    implementation(libs.com.fasterxml.jackson.dataformat.jackson.dataformat.yaml)

    // JLINE
    implementation(libs.org.jline)
    implementation(libs.org.jline.jline.terminal.jansi)
    implementation(libs.org.jline.jline.console)
    implementation(libs.org.jline.jline.console.ui)

    // JETBRAINS ANNOTATIONS
    compileOnly(libs.org.jetbrains.annotations)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.annotationProcessorPath = configurations.annotationProcessor.get()
}

