import org.gradle.kotlin.dsl.support.serviceOf
import org.gradle.nativeplatform.internal.DefaultTargetMachineFactory
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"
    id("org.sirekanyan.version-checker") version "1.0.14"
    distribution
}

group = "org.sirekanyan"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    val isRelease = hasProperty("release")
    val host = gradle.serviceOf<DefaultTargetMachineFactory>().host()
    val os = host.operatingSystemFamily
    val arch = host.architecture
    buildList {
        if (isRelease) {
            add(linuxX64())
            add(linuxArm64())
            add(mingwX64())
            if (os.isMacOs) {
                add(macosX64())
                add(macosArm64())
            }
        } else {
            add(
                when {
                    os.isLinux && arch.isX64() -> linuxX64()
                    os.isLinux && arch.isArm64() -> linuxArm64()
                    os.isWindows && arch.isX64() -> mingwX64()
                    os.isMacOs && arch.isX64() -> macosX64()
                    os.isMacOs && arch.isArm64() -> macosArm64()
                    else -> error("Unsupported host: $host")
                }
            )
        }
    }.forEach {
        it.binaries {
            executable(listOf(if (isRelease) RELEASE else DEBUG))
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation("com.github.ajalt.clikt:clikt-core:5.1.0")
                implementation("io.ktor:ktor-client-curl:3.4.0")
                implementation("io.ktor:ktor-client-content-negotiation:3.4.0")
                implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.0")
            }
        }
    }
}

private val kotlinTargetNames: List<String> =
    kotlin.targets.filterIsInstance<KotlinNativeTarget>().map { it.name }

distributions {
    kotlinTargetNames.forEach { targetName ->
        create(targetName) {
            distributionBaseName = "translate-${targetName.replace("X64", "-amd64").replace("Arm64", "-arm64")}"
            contents {
                from("build/bin/${targetName}/releaseExecutable/translate.exe")
                from("build/bin/${targetName}/releaseExecutable/translate.kexe") {
                    rename { it.removeSuffix(".kexe") }
                }
            }
        }
    }
}

kotlinTargetNames.forEach { targetName ->
    tasks {
        getByName<Tar>("${targetName}DistTar") {
            filePermissions { unix("755") }
            compression = Compression.GZIP
            archiveExtension = "tar.gz"
        }
    }
}

private fun MachineArchitecture.isX64(): Boolean {
    return name == MachineArchitecture.X86_64
}

@Suppress("UnstableApiUsage")
private fun MachineArchitecture.isArm64(): Boolean {
    return name == MachineArchitecture.ARM64
}
