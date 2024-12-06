plugins {
    kotlin("jvm") version libs.versions.kotlin
    id("maven-publish")
}

group = Publishing.GroupId
version = Publishing.Version

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.soberg.aoc.api)

    testImplementation(libs.test.assertk)
    testImplementation(libs.test.junitJupiter)
    testImplementation(libs.test.junitJupiter.params)
    testImplementation(libs.test.kotlin.coroutines)
    testImplementation(libs.test.mockk)
}

tasks.withType<Test> {
    useJUnitPlatform()
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