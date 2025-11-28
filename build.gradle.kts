import org.gradle.kotlin.dsl.testRuntimeOnly

plugins {
    kotlin("jvm") version libs.versions.kotlin
    id("maven-publish")
}

group = Publishing.GroupId
version = Publishing.Version

dependencies {
    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.stdlib)
    implementation(libs.soberg.aoc.api)

    testImplementation(platform(libs.test.junit.bom))
    testRuntimeOnly(libs.test.junit.launcher)
    testImplementation(libs.test.junitJupiter)
    testImplementation(libs.test.junitJupiter.params)

    testImplementation(libs.test.assertk)
    testImplementation(libs.test.kotlin.coroutines)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_21.toString()))
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = Publishing.ArtifactId
            groupId = Publishing.GroupId
            version = Publishing.Version

            from(components["java"])
        }
    }
}