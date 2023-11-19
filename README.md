# CIRE |-| Custom Item Resource Expansion

![Deployment](https://github.com/theDevJade/cire/workflows/publish/badge.svg)

## Overview

Custom Item Library is a powerful and easy-to-use library for PaperMC that simplifies the creation and management of custom items in Minecraft. Designed for both beginners and advanced users, this library offers a range of functionalities to enhance your Minecraft server experience.

## Features

- Easy custom item creation
- Advanced item customization
- Efficient performance with PaperMC

### Requirements

- PaperMC
- Java 17 or higher

### Installation

#### Gradle

```kotlin
repositories {
  maven("https://maven.pkg.github.com/theDevJade/cire")
}

dependencies {
  implementation("com.thedevjade.cire:cire:latest")
}
```

```groovy
repositories {
  maven { url = "https://maven.pkg.github.com/theDevJade/cire" }
}

dependencies {
  implementation("com.thedevjade.cire:cire:latest")
}
```

```maven
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/theDevJade/cire</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.thedevjade.cire</groupId>
        <artifactId>cire</artifactId>
        <version>latest</version>
    </dependency>
</dependencies>
```

# Documentation

For detailed documentation and advanced usage, please visit our Wiki.

# Contributing

Contributions are welcome! Please read our Contributing Guidelines for more information.

# License

This project is licensed under the MIT License.

