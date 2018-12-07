plugins {
    java
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
    annotationProcessor("org.projectlombok:lombok:1.18.4")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.4")
    testImplementation("org.projectlombok:lombok:1.18.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.3.2")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.2")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.withType<Test> {
    useJUnitPlatform {}
}
