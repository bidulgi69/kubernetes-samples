import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version Version.springBootVersion
    id("io.spring.dependency-management") version Version.dependencyManagementVersion
    kotlin("jvm") version Version.kotlinVersion
    kotlin("plugin.spring") version Version.kotlinVersion

    id("application")
}

repositories {
    mavenCentral()
}

allprojects {
    group = "kr.dove"
    version = "1.0-SNAPSHOT"

    tasks.withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }
}

subprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    apply {
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("application")
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${Version.springCloudVersion}")
        }
    }
}