import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class BuildConfigSecretsConventionPlugins : Plugin<Project> {

  companion object {
    const val APP_ID_KEY = "APPLICATION_ID"
    const val VERSION_CODE_KEY = "VERSION_CODE"
    const val VERSION_NAME_KEY = "VERSION_NAME"
    const val TOKEN_KEY = "TOKEN"
  }

  override fun apply(target: Project) {
    val libs = target.extensions.getByType<VersionCatalogsExtension>().named("libs")
    when {
      target.pluginManager.hasPlugin(
        libs.findPlugin("android-application").get().get().pluginId
      ) -> target.extensions.configure<ApplicationExtension> {
        setupSecrets(target, this)
      }

      target.pluginManager.hasPlugin(
        libs.findPlugin("android-library").get().get().pluginId
      ) -> target.extensions.configure<LibraryExtension> {
        setupSecrets(target, this)
      }
    }
  }

  private fun setupSecrets(project: Project, extension: CommonExtension<*, *, *, *, *, *>) {
    with(extension) {
      buildFeatures {
        buildConfig = true
      }
      defaultConfig {
        val localProperties = project.rootProject.file("app.properties")
        val localPropertiesMap = localProperties.inputStream().use { input ->
          val properties = java.util.Properties()
          properties.load(input)
          properties
        }
        buildConfigField("String", APP_ID_KEY, localPropertiesMap.getProperty(APP_ID_KEY))
        buildConfigField("int", VERSION_CODE_KEY, localPropertiesMap.getProperty(VERSION_CODE_KEY))
        buildConfigField(
          "String", VERSION_NAME_KEY, localPropertiesMap.getProperty(VERSION_NAME_KEY)
        )

        val token = project.rootProject.file("local.properties")
        val tokenProperties = token.inputStream().use { input ->
          val properties = java.util.Properties()
          properties.load(input)
          properties
        }
        buildConfigField("String", TOKEN_KEY, tokenProperties.getProperty(TOKEN_KEY))
      }
    }

  }
}