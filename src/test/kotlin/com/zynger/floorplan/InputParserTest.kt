package com.zynger.floorplan

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import java.lang.UnsupportedOperationException


class InputParserTest {
    
    @Test
    fun `parses absolute file paths`() {
        val path = "samples/db.json"

        val input = InputParser.parse(path)

        assertEquals(path, input.schemaPath)
    }

    @Test
    fun `parses home relative file paths`() {
        val path = "~/foo/bar/db.json"
        val expected = System.getProperty("user.home") + "/foo/bar/db.json"

        val input = InputParser.parse(path)

        assertEquals(expected, input.schemaPath)
    }

    @Test
    fun `do not support home directory expansion for explicit user names`() {
        val path = "~otheruser/Documents/foo/bar/db.json"

        try {
            InputParser.parse(path)
            fail()
        } catch(e: UnsupportedOperationException) {
            // ok
        }
    }
}
