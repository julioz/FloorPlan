package com.zynger.floorplan

import org.junit.Assert.*
import org.junit.Test
import java.lang.IllegalArgumentException
import java.lang.UnsupportedOperationException


class InputParserTest {
    
    @Test
    fun `parses absolute file paths`() {
        val path = "samples/db.json"

        val input = InputParser.parse(arrayOf(path))

        assertEquals(path, input.schemaPath)
    }

    @Test
    fun `parses home relative file paths`() {
        val path = "~/foo/bar/db.json"
        val expected = System.getProperty("user.home") + "/foo/bar/db.json"

        val input = InputParser.parse(arrayOf(path))

        assertEquals(expected, input.schemaPath)
    }

    @Test
    fun `do not support home directory expansion for explicit user names`() {
        val path = "~otheruser/Documents/foo/bar/db.json"

        try {
            InputParser.parse(arrayOf(path))
            fail()
        } catch(e: UnsupportedOperationException) {
            // ok
        }
    }

    @Test
    fun `does not pick up output path argument if not specified`() {
        val path = "samples/db.json"

        val input = InputParser.parse(arrayOf(path))

        assertNull(input.outputPath)
    }

    @Test
    fun `throws when output argument is specified with no value`() {
        val args = "samples/db.json --output="
            .split(" ")
            .toTypedArray()

        try {
            InputParser.parse(args)
            fail()
        } catch (e: IllegalArgumentException) {
            // ok
        }
    }

    @Test
    fun `parses absolute file output paths`() {
        val args = "samples/db.json --output=result/mydb.dbml"
            .split(" ")
            .toTypedArray()

        val input = InputParser.parse(args)

        assertEquals("result/mydb.dbml", input.outputPath)
    }

    @Test
    fun `parses absolute file output paths by ignoring following arguments`() {
        val args = "samples/db.json --output=result/mydb.dbml --something-else"
            .split(" ")
            .toTypedArray()

        val input = InputParser.parse(args)

        assertEquals("result/mydb.dbml", input.outputPath)
    }

    @Test
    fun `parses relative file output paths`() {
        val args = "samples/db.json --output=~/foo/bar/result/mydb.dbml"
            .split(" ")
            .toTypedArray()
        val expected = System.getProperty("user.home") + "/foo/bar/result/mydb.dbml"

        val input = InputParser.parse(args)

        assertEquals(expected, input.outputPath)
    }

    @Test
    fun `creation sql is not part of table notes when argument isn't specified`() {
        val input = InputParser.parse(
            arrayOf(
                "samples/db.json"
            )
        )

        assertFalse(input.creationSqlAsTableNote)
    }

    @Test
    fun `creation sql is part of table notes when argument is specified`() {
        val input = InputParser.parse(
            arrayOf(
                "samples/db.json",
                "--creation-sql-as-table-note"
            )
        )

        assertTrue(input.creationSqlAsTableNote)
    }

    @Test
    fun `nullable fields aren't rendered when argument isn't specified`() {
        val input = InputParser.parse(
            arrayOf(
                "samples/db.json"
            )
        )

        assertFalse(input.renderNullableFields)
    }

    @Test
    fun `nullable fields are rendered when argument is specified`() {
        val input = InputParser.parse(
            arrayOf(
                "samples/db.json",
                "--render-nullable-fields"
            )
        )

        assertTrue(input.renderNullableFields)
    }
}
