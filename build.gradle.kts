import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "4.0.3"
}

group = "io.github.alexbogovich"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.projectlombok:lombok:1.18.4")
    implementation("org.slf4j:slf4j-simple:1.7.25")
    implementation("org.slf4j:slf4j-api:1.7.25")
    implementation("com.google.guava:guava:27.0.1-jre")
    implementation("commons-cli:commons-cli:1.4")
    annotationProcessor("org.projectlombok:lombok:1.18.4")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.4")
    testImplementation("org.projectlombok:lombok:1.18.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.3.2")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClassName = "io.github.alexbogovich.treevalidator.TreeValidator"
}

tasks.withType<Test> {
    useJUnitPlatform {}
}