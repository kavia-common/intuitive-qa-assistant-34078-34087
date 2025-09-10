androidApplication {
    namespace = "org.example.app"

    // Use the ecosystem-provided testing config; add module-local test dependencies here.
    testing {
        dependencies {
            // JUnit 5 (optional; some environments may not run it for unit tests)
            implementation("org.junit.jupiter:junit-jupiter:5.10.2")
            runtimeOnly("org.junit.platform:junit-platform-launcher")
            // JUnit 4 for guaranteed discovery of unit tests
            implementation("junit:junit:4.13.2")
        }
    }

    dependencies {
        implementation("org.apache.commons:commons-text:1.11.0")
        implementation(project(":utilities"))

        // AndroidX core + AppCompat with explicit versions
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
    }
}
