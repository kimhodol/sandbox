plugins {
    kotlin("jvm") version "1.4.32"
}

group = "dev.hodol"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val jar: Jar by tasks
jar.enabled = true

dependencies {
    implementation(kotlin("stdlib"))
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "kotlin")

    dependencies {
        implementation(kotlin("stdlib"))
        runtimeOnly("org.apache.logging.log4j:log4j-core:2.13.3")
    }
}
