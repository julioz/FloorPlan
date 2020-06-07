package com.zynger.floorplan.gradle

import com.google.common.truth.Truth.*
import com.zynger.floorplan.DbmlConfiguration
import com.zynger.floorplan.Format
import com.zynger.floorplan.gradle.model.OutputFormat
import org.junit.Test
import java.io.File
import com.zynger.floorplan.gradle.model.DbmlConfiguration as GradlePluginDbmlConfiguration

class OutputParameterHandlerTest {

    @Test
    fun `dbml gets mapped to output format`() {
        val outputParameterHandler = OutputParameterHandler(SCHEMA, SCHEMA_LOCATION)

        val format = outputParameterHandler.format(OutputFormat.DBML(GradlePluginDbmlConfiguration()))

        assertThat(format).isEqualTo(Format.DBML(DbmlConfiguration()))
    }

    @Test
    fun `svg gets mapped to output format`() {
        val outputParameterHandler = OutputParameterHandler(SCHEMA, SCHEMA_LOCATION)

        val format = outputParameterHandler.format(OutputFormat.SVG)

        assertThat(format).isEqualTo(Format.SVG)
    }

    @Test
    fun `png gets mapped to output format`() {
        val outputParameterHandler = OutputParameterHandler(SCHEMA, SCHEMA_LOCATION)

        val format = outputParameterHandler.format(OutputFormat.PNG)

        assertThat(format).isEqualTo(Format.PNG)
    }

    @Test
    fun `dot gets mapped to output format`() {
        val outputParameterHandler = OutputParameterHandler(SCHEMA, SCHEMA_LOCATION)

        val format = outputParameterHandler.format(OutputFormat.DOT)

        assertThat(format).isEqualTo(Format.DOT)
    }

    @Test
    fun `output file name uses output format extension`() {
        val outputParameterHandler = OutputParameterHandler(SCHEMA, SCHEMA_LOCATION)

        val outputFile = outputParameterHandler.file(OUTPUT_LOCATION, Format.SVG)

        assertThat(outputFile.extension).isEqualTo(Format.SVG.extension)
    }

    @Test
    fun `output file is stored in output location when there are no subdirectories`() {
        val schema = File(SCHEMA_LOCATION, SCHEMA_FILE_NAME)
        val outputParameterHandler = OutputParameterHandler(schema, SCHEMA_LOCATION)

        val outputFile = outputParameterHandler.file(OUTPUT_LOCATION, Format.SVG)

        val expected = File(OUTPUT_LOCATION, "1.svg")
        assertThat(outputFile.path).isEqualTo(expected.path)
    }

    @Test
    fun `output file is stored in output sub-directory location when there is a subdirectories`() {
        val schema = File("$SCHEMA_LOCATION${File.separator}subdirectory", SCHEMA_FILE_NAME)
        val outputParameterHandler = OutputParameterHandler(schema, SCHEMA_LOCATION)

        val outputFile = outputParameterHandler.file(OUTPUT_LOCATION, Format.SVG)

        val expected = File("$OUTPUT_LOCATION${File.separator}subdirectory", "1.svg")
        assertThat(outputFile.path).isEqualTo(expected.path)
    }

    @Test
    fun `output file is stored in output sub-directory location when there are multiple subdirectories`() {
        val schema = File("$SCHEMA_LOCATION${File.separator}subdirectory${File.separator}sub-dir2", SCHEMA_FILE_NAME)
        val outputParameterHandler = OutputParameterHandler(schema, SCHEMA_LOCATION)

        val outputFile = outputParameterHandler.file(OUTPUT_LOCATION, Format.SVG)

        val expected = File("$OUTPUT_LOCATION${File.separator}subdirectory${File.separator}sub-dir2", "1.svg")
        assertThat(outputFile.path).isEqualTo(expected.path)
    }

    private companion object {
        private const val SCHEMA_FILE_NAME = "1.json"
        private val SCHEMA = File(SCHEMA_FILE_NAME)
        private val SCHEMA_LOCATION = File("/sample-android-project/schemas")
        private val OUTPUT_LOCATION = File("/sample-android-project/outputs")
    }
}
