import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.File

/**
 * 자동 버전관리, git action에 의해 실행됨
 *
 * example)
 *  ./gradlew versionUp -Pversioning=minor
 */
abstract class IncreaseVersionNameAndCode : DefaultTask() {

    @Option(option = "versioning", description = "Versioning type: major, minor, patch")
    @Input
    var versioning: String = "patch"

    @TaskAction
    fun versionUp() {
        val file = File("./app/build.gradle.kts")
        var content = file.readText()

        val versionCodeRegex = "versionCode\\s*=\\s*(\\d+)".toRegex()
        val versionNameRegex = "versionName\\s*=\\s*\"(\\d+\\.\\d+\\.\\d+)\"".toRegex()

        content = versionCodeRegex.replace(content) { match ->
            val currentCode = match.groupValues[1].toInt()
            "versionCode = ${currentCode + 1}"
        }

        content = versionNameRegex.replace(content) { match ->
            val currentVersion = match.groupValues[1]
            val newVersion = getNewVersionName(currentVersion, versioning)
            "versionName = \"$newVersion\""
        }

        file.writeText(content)
        println("complete version update")
    }

    private fun getNewVersionName(version: String, type: String): String {
        val (major, minor, patch) = version.split(".").map { it.toInt() }
        return when (type) {
            "major" -> "${major + 1}.0.0"
            "minor" -> "$major.${minor + 1}.0"
            else -> "$major.$minor.${patch + 1}"
        }
    }
}
