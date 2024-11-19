// settings.gradle.kts

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        // Repositorio de JitPack
        maven { url = uri("https://jitpack.io") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        // Repositorio de JitPack
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "TiendaOnline"
include(":app")