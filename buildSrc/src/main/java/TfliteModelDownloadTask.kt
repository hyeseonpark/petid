import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.OutputFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Input
import org.gradle.work.InputChanges
import java.net.URL
import javax.inject.Inject

/**
 * google drive 에서 .tflite file 을 다운로드 하는 task
 */
abstract class TfliteModelDownloadTask @Inject constructor() : DefaultTask() {

    @Input
    var modelUrl: String = ""
        private set

    @Input
    var modelFileName: String = ""
        private set

    fun setModelUrl(url: String) {
        modelUrl = url
    }

    fun setModelFileName(fileName: String) {
        modelFileName = fileName
    }

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun downloadModel() {
        val modelFile = outputFile.get().asFile

        if (!modelFile.exists()) {
            modelFile.parentFile.mkdirs()
            logger.lifecycle("Downloading TFLite model from: $modelUrl")

            try {
                URL(modelUrl).openStream().use { input ->
                    modelFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                logger.lifecycle("TFLite model downloaded to: ${modelFile.absolutePath}")
            } catch (e: Exception) {
                logger.error("Failed to download TFLite model: ${e.message}")
                throw RuntimeException("Model download failed", e)
            }
        } else {
            logger.lifecycle("TFLite model already exists, skipping download.")
        }
    }
}