buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath(kotlin("gradle-plugin", Dependencies.kotlinVersion))
        classpath("com.github.ben-manes:gradle-versions-plugin:0.36.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
    dependencies{
        apply("$rootDir/buildSrc/src/main/java/Version.gradle")
    }
}

subprojects {
    apply("$rootDir/buildSrc/src/main/java/DependencyReport.gradle")
}