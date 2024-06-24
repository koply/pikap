plugins {
    java
    `maven-publish`
    application
    alias(libs.plugins.com.github.johnrengelman.shadow) apply true
}

group = "me.koply"
version = "1.0.0-alpha"
description = "Pikap"

dependencies {
    implementation(libs.dev.arbjerg.lavaplayer)
    implementation(libs.dev.lavalink.youtube.v2)
    implementation(libs.org.slf4j.slf4j.simple)
    implementation(libs.com.github.kwhat.jnativehook)
    implementation(libs.com.github.tomas.langer.chalk)
    implementation(libs.net.oneandone.reflections8.reflections8)
    implementation(libs.com.github.jncrmx.discord.game.sdk4j)
    implementation(libs.org.xerial.sqlite.jdbc)
    implementation(libs.com.j256.ormlite.ormlite.core)
    implementation(libs.com.j256.ormlite.ormlite.jdbc)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClass = "me.koply.pikap.Main"
    applicationDefaultJvmArgs = listOf("-Dfile.encoding=UTF-8")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks {
    jar {
        manifest {
            attributes["Implementation-Title"] = project.name
            attributes["Implementation-Version"] = project.version
            attributes["Manifest-Version"] = "1.0"
            attributes["Main-Class"] = "me.koply.pikap.Main"
        }
    }

    named<JavaExec>("run") {
        standardInput = System.`in`
    }

    shadowJar {
        minimize {
            exclude(dependency(libs.org.xerial.sqlite.jdbc.get()))
        }
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
}