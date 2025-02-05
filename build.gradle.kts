plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
}

extra["axonVersion"] = "4.9.4"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    version = "0.0.1-SNAPSHOT"

    apply {
        plugin("io.spring.dependency-management")
        plugin("org.springframework.boot")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }

    dependencyManagement {
        imports {
            mavenBom("org.axonframework:axon-bom:${property("axonVersion")}")
        }
    }
}
