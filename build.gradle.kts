/*
 * Copyright (c) 2023. Made by theDevJade or contributors.
 */

plugins {
  id("java")
  kotlin("jvm") version "1.9.20"
}


val mcVersion = "1.20.2"
group = "com.theDevJade"
version = "1.0-RELEASE"

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/") {
    name = "papermc-repo"
  }
  maven("https://oss.sonatype.org/content/groups/public/") {
    name = "sonatype"
  }
}

dependencies {
  compileOnly("io.papermc.paper:paper-api:${mcVersion}-R0.1-SNAPSHOT")
  implementation(kotlin("stdlib-jdk8"))
}

sourceSets {
  main {
    java {
      srcDir("src/main/kotlin")
    }
  }
}

val targetJavaVersion = 17
java {
  val javaVersion = JavaVersion.toVersion(targetJavaVersion)
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
  if (JavaVersion.current() < javaVersion) {
    toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
  }
}

tasks.jar {
  archiveBaseName.set("cire")
  // Optionally, you can also set the version if needed
  archiveVersion.set("1")
}

tasks.withType<JavaCompile>().configureEach {
  if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
    options.release.set(targetJavaVersion)
  }
}

tasks.named<ProcessResources>("processResources") {
  val props = mapOf("version" to project.version)
  inputs.properties(props)
  filteringCharset = "UTF-8"
  filesMatching("paper-plugin.yml") {
    expand(props)
  }
}

kotlin {
  jvmToolchain(17)
}
