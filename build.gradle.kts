import com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    id("com.netflix.dgs.codegen") version "5.1.17"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    // jacoco
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11
// extra["kotlin.version"] = "1.4.31"

repositories {
    mavenCentral()
}

dependencies {
    // implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.netflix.graphql.dgs:graphql-dgs-webflux-starter")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:5.0.5"))
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<GenerateJavaTask> {
    generateClient = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
