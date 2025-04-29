plugins {
    id("java")
    application
}

group = "me.koply"
version = "1.0-SNAPSHOT"
description = "Pikap"

application {
    mainClass = "me.koply.pikap2.Main"
    applicationDefaultJvmArgs = listOf("-Dfile.encoding=UTF-8")
}

repositories {
    mavenCentral()
    maven(url = "https://maven.lavalink.dev/releases/")
    maven(url = "https://jitpack.io/")
}

dependencies {
    // REFLECTION
    implementation(libs.net.oneandone.reflections8.reflections8)

    // LAVAPLAYER
    implementation(libs.dev.arbjerg.lavaplayer)
    implementation(libs.dev.lavalink.youtube.v2)

    // SQL
    implementation(libs.org.xerial.sqlite.jdbc)
    implementation(libs.com.j256.ormlite.ormlite.core)
    implementation(libs.com.j256.ormlite.ormlite.jdbc)

    // LOGGING
    implementation(libs.org.slf4j.slf4j.simple)

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
    implementation(libs.com.google.dagger.compiler)

    // JACKSON
    implementation(libs.com.fasterxml.jackson.databind.jackson.databind)
    implementation(libs.com.fasterxml.jackson.dataformat.jackson.dataformat.yaml)
}

tasks.test {
    useJUnitPlatform()
}