package com.zynger.floorplan

import java.io.File

object InputParser {

    data class Input(
        val schemaPath: String,
        val outputPath: String?,
        val formats: List<String>?,
        val creationSqlAsTableNote: Boolean,
        val renderNullableFields: Boolean
    )

    private const val OUTPUT_ARG_KEY: String = "output"
    private const val FORMAT_ARG_KEY: String = "format"
    private const val CREATION_SQL_AS_TABLE_NOTES_ARG_KEY: String = "creation-sql-as-table-note"
    private const val RENDER_NULLABLE_FIELDS_ARG_KEY: String = "render-nullable-fields"

    fun parse(args: Array<String>): Input {
        require(args.isNotEmpty()) {
            val onlyDbmlNote = "[note: only for DBML outputs]"
            """
            Pass the source Room JSON schema as an argument.
            Optionally, use:
            
            * $OUTPUT_ARG_KEY: specify an output file for the rendering content.
            * $FORMAT_ARG_KEY: specify an output format for the rendering content [one or more of DBML, SVG, PNG, DOT].
            * $CREATION_SQL_AS_TABLE_NOTES_ARG_KEY: adds the SQL used to create tables as notes $onlyDbmlNote.
            * $RENDER_NULLABLE_FIELDS_ARG_KEY: changes the rendering of the data type of nullable fields $onlyDbmlNote.
            
            e.g.: gradlew run --args="<path-to-schema-file> [--$OUTPUT_ARG_KEY=<output-file-path>] [--$FORMAT_ARG_KEY=<output-format>] [--$CREATION_SQL_AS_TABLE_NOTES_ARG_KEY] [--$RENDER_NULLABLE_FIELDS_ARG_KEY]"
        """.trimIndent()
        }

        val inputFilePath: String = sanitizeFilePath(args.first())
        val outputFilePath: String? = args.getArgumentValue(OUTPUT_ARG_KEY)?.let { sanitizeFilePath(it) }
        val formats: List<String>? = args.getArgumentList(FORMAT_ARG_KEY)
        val noteCreationSql = args.argumentExists(CREATION_SQL_AS_TABLE_NOTES_ARG_KEY)
        val renderNullableFields = args.argumentExists(RENDER_NULLABLE_FIELDS_ARG_KEY)

        return Input(inputFilePath, outputFilePath, formats, noteCreationSql, renderNullableFields)
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

    private fun Array<String>.getArgumentList(argKey: String, delimiter: String = ","): List<String>? {
        return if (none { it.contains("--$argKey=") }) {
            null
        } else {
            val argumentPair = find { it.contains("--$argKey=") }!!
            require(argumentPair.substringAfter("=").trim().isNotEmpty()) {
                "Please inform a value for the --$argKey argument."
            }

            argumentPair.substringAfter("=").substringBefore(" ").split(delimiters = *arrayOf(delimiter))
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
