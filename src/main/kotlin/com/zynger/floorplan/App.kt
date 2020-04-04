package com.zynger.floorplan

import com.zynger.floorplan.dbml.Table
import com.zynger.floorplan.model.Schema
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File

fun main(args: Array<String>) {
    require(args.isNotEmpty()) {
        """
            Pass the source Room JSON schema as an argument.
            
            e.g.: gradlew run --args=<path-to-schema-file>
        """.trimIndent()
    }

    val src = File(sanitizeInputFilePath(args.first()))
    val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true, isLenient = true))

    val dbml = json
        .parse(Schema.serializer(), src.readText())
        .database
        .entities
        .map { Table(it) }
        .joinToString(separator = "\n\n")
    print(dbml)
}

private fun sanitizeInputFilePath(inputFilePath: String): String {
    return when {
        inputFilePath.startsWith("~" + File.separator) -> System.getProperty("user.home") + inputFilePath.substring(1)
        inputFilePath.startsWith("~") -> {
            // Don't support explicit username relative paths as '~otheruser/Documents'
            // https://stackoverflow.com/a/7163446
            throw UnsupportedOperationException("Home directory expansion is not supported for explicit user-names.\n" +
                    "Provide an absolute path to your input schema file.")
        }
        else -> inputFilePath
    }
}
