plugins {
    kotlin("multiplatform") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"
    id("org.sirekanyan.version-checker") version "1.0.14"
}

group = "org.sirekanyan"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    linuxX64 {
        binaries {
            executable(listOf(if (hasProperty("release")) RELEASE else DEBUG))
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation("com.github.ajalt.clikt:clikt-core:5.1.0")
                implementation("io.ktor:ktor-client-core:3.4.0")
                implementation("io.ktor:ktor-client-content-negotiation:3.4.0")
                implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.0")
            }
        }
        linuxX64Main {
            dependencies {
                implementation("io.ktor:ktor-client-curl:3.4.0")
            }
        }
    }
}
