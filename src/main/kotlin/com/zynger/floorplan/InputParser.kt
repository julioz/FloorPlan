package com.zynger.floorplan

import java.io.File

object InputParser {

    data class Input(
        val schemaPath: String,
        val outputPath: String?
    )

    private const val OUTPUT_ARG_KEY: String = "output"

    fun parse(args: Array<String>): Input {
        require(args.isNotEmpty()) {
            """
            Pass the source Room JSON schema as an argument.
            Optionally, specify an output file for the DBML representation.
            
            e.g.: gradlew run --args="<path-to-schema-file> [--output=<output-file-path>]"
        """.trimIndent()
        }

        val inputFilePath: String = sanitizeFilePath(args.first())
        val outputFilePath: String? = if (args.containsOutputArg()) sanitizeFilePath(args.extractOutputArg()) else null

        return Input(inputFilePath, outputFilePath)
    }

    private fun Array<String>.containsOutputArg(): Boolean {
        return any { it.contains("--$OUTPUT_ARG_KEY=") }
    }

    private fun Array<String>.extractOutputArg(): String {
        val outputFileArgumentPair = find { it.contains("--$OUTPUT_ARG_KEY=") }!!
        require(outputFileArgumentPair.substringAfter("=").trim().isNotEmpty()) {
            "Please inform an output file path with the --output argument."
        }

        return outputFileArgumentPair.substringAfter("=").substringBefore(" ")
    }

    private fun sanitizeFilePath(path: String): String {
        return when {
            path.startsWith("~" + File.separator) -> System.getProperty("user.home") + path.substring(1)
            path.startsWith("~") -> {
                // Don't support explicit username relative paths as '~otheruser/Documents'
                // https://stackoverflow.com/a/7163446
                throw UnsupportedOperationException("Home directory expansion is not supported for explicit user names.\n" +
                        "Provide an absolute path to your file.")
            }
            else -> path
        }
    }
}
