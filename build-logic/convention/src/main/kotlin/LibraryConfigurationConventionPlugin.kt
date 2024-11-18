import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class LibraryConfigurationConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    val libs = target.extensions.getByType<VersionCatalogsExtension>().named("libs")

    if (target.pluginManager.hasPlugin(
        libs.findPlugin("android-library").get().get().pluginId
      )
    ) {
      target.project.configure<LibraryExtension> {
        target.project.configureKotlinAndroid(this)
      }
    }
  }

  private fun Project.configureKotlinAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
  ) {
    commonExtension.apply {
      compileSdk = 35

      defaultConfig {
        minSdk = 23
      }

      compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
      }

      project.configure<KotlinAndroidProjectExtension> {
        this.apply {
          compilerOptions.apply {
            jvmTarget = JvmTarget.JVM_17
            freeCompilerArgs.add(
              "-Xexplicit-api=strict"
            )
          }
        }
      }
    }
  }
}