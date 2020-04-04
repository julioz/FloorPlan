package com.zynger.floorplan

import java.io.File

object InputParser {

    data class Input(val schemaPath: String)

    fun parse(inputFilePath: String): Input {
        return Input(sanitizeInputFilePath(inputFilePath))
    }

    private fun sanitizeInputFilePath(inputFilePath: String): String {
        return when {
            inputFilePath.startsWith("~" + File.separator) -> System.getProperty("user.home") + inputFilePath.substring(1)
            inputFilePath.startsWith("~") -> {
                // Don't support explicit username relative paths as '~otheruser/Documents'
                // https://stackoverflow.com/a/7163446
                throw UnsupportedOperationException("Home directory expansion is not supported for explicit user names.\n" +
                        "Provide an absolute path to your input schema file.")
            }
            else -> inputFilePath
        }
    }
}
