package com.zynger.floorplan

import java.io.File

object InputParser {

    data class Input(
        val schemaPath: String,
        val outputPath: String?,
        val creationSqlAsTableNote: Boolean
    )

    private const val OUTPUT_ARG_KEY: String = "output"
    private const val CREATION_SQL_AS_TABLE_NOTES_ARG_KEY: String = "creation-sql-as-table-note"

    fun parse(args: Array<String>): Input {
        require(args.isNotEmpty()) {
            """
            Pass the source Room JSON schema as an argument.
            Optionally, use:
            
            * output: specify an output file for the DBML representation.
            * creation-sql-as-table-note: adds the SQL used to create tables as notes in their DBML representation.
            
            e.g.: gradlew run --args="<path-to-schema-file> [--output=<output-file-path>] [--creation-sql-as-table-note]"
        """.trimIndent()
        }

        val inputFilePath: String = sanitizeFilePath(args.first())
        val outputFilePath: String? = args.getArgumentValue(OUTPUT_ARG_KEY)?.let { sanitizeFilePath(it) }
        val noteCreationSql = args.argumentExists(CREATION_SQL_AS_TABLE_NOTES_ARG_KEY)

        return Input(inputFilePath, outputFilePath, noteCreationSql)
    }

    private fun Array<String>.getArgumentValue(argKey: String): String? {
        return if (none { it.contains("--$argKey=") }) {
            null
        } else {
            val argumentPair = find { it.contains("--$argKey=") }!!
            require(argumentPair.substringAfter("=").trim().isNotEmpty()) {
                "Please inform a value for the --$argKey argument."
            }

            argumentPair.substringAfter("=").substringBefore(" ")
        }
    }

    private fun Array<String>.argumentExists(argKey: String): Boolean {
        return any { it == "--$argKey" }
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
