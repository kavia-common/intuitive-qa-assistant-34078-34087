androidApplication {
    namespace = "org.example.app"

    dependencies {
        // AndroidX core UI libraries (explicit versions as required)
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")

        // Networking: OkHttp + Retrofit + Moshi
        implementation("com.squareup.okhttp3:okhttp:4.12.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
        implementation("com.squareup.retrofit2:retrofit:2.11.0")
        implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
        implementation("com.squareup.moshi:moshi-kotlin:1.15.1")

        // Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

        // Remove previous demo deps
        // implementation("org.apache.commons:commons-text:1.11.0")
        // implementation(project(":utilities"))
    }
}
