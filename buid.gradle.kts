// Top-level build file
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.0" apply false
    id("org.springframework.boot") version "3.1.0" apply false
    id("io.spring.dependency-management") version "1.1.0" apply false
}

// Configure all projects
allprojects {
    group = "com.yourname"
    version = "1.0.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

// Configure subprojects (all modules)
subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "io.spring.dependency-management")

    dependencies {
        // Common dependencies for all modules
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        
        // Testing
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }
}