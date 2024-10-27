plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.ksp.plugin)
  id("vlr.secrets")
  id("dagger.hilt.android.plugin")
}

android {
  namespace = "dev.staticvar.vlr.core"
  compileSdk = 35

  defaultConfig {
    minSdk = 23

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    isCoreLibraryDesugaringEnabled = true
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
}

dependencies {

  implementation(libs.bundles.hilt)
  ksp(libs.hilt.compiler)

  implementation(libs.kotlinx.serialization)

  implementation(libs.bundles.ktor)
  implementation(libs.logging.interceptor)

  coreLibraryDesugaring(libs.core.desugar)
}