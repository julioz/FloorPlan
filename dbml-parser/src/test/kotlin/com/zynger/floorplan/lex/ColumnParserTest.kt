package com.zynger.floorplan.lex

import com.zynger.floorplan.Column
import org.junit.Assert.*
import org.junit.Test

class ColumnParserTest {

    companion object {
        private const val TABLE_NAME = "myTable"
    }

    @Test
    fun `empty columns`() {
        val columnsInput = """
        """.trimIndent()

        val columns = ColumnParser.parseColumns(TABLE_NAME, columnsInput)

        assertEquals(emptyList<Column>(), columns)
    }

    @Test
    fun `parses name and type`() {
        val columnsInput = """
            id int
            name varchar
            age int
            customer bool
            image blob

        """.trimIndent()

        val columns = ColumnParser.parseColumns(TABLE_NAME, columnsInput)

        assertEquals("id", columns[0].name)
        assertEquals("int", columns[0].type)

        assertEquals("name", columns[1].name)
        assertEquals("varchar", columns[1].type)

        assertEquals("age", columns[2].name)
        assertEquals("int", columns[2].type)

        assertEquals("customer", columns[3].name)
        assertEquals("bool", columns[3].type)

        assertEquals("image", columns[4].name)
        assertEquals("blob", columns[4].type)
    }

    @Test
    fun `parses name without surrounding quotes`() {
        val columnsInput = """
            "id" int

        """.trimIndent()

        val columns = ColumnParser.parseColumns(TABLE_NAME, columnsInput)

        assertEquals("id", columns[0].name)
        assertEquals("int", columns[0].type)
    }

    @Test
    fun `parses primary key`() {
        val columnsInput = """
            id int [pk]

        """.trimIndent()

        val columns = ColumnParser.parseColumns(TABLE_NAME, columnsInput)

        assertEquals(true, columns.first().primaryKey)
    }

    @Test
    fun `parses not null`() {
        val columnsInput = """
            age int [not null]

        """.trimIndent()

        val columns = ColumnParser.parseColumns(TABLE_NAME, columnsInput)

        assertEquals(true, columns.first().notNull)
    }

    @Test
    fun `parses primary key, not null and increment`() {
        val columnsInput = """
            id int [pk, increment, not null]

        """.trimIndent()

        val columns = ColumnParser.parseColumns(TABLE_NAME, columnsInput)

        assertEquals(true, columns.first().primaryKey)
        assertEquals(true, columns.first().notNull)
        assertEquals(true, columns.first().increment)
    }

    @Test
    fun `parses primary key, not null and increment ignoring spaces`() {
        val columnsInput = """
            id int [pk,increment,         not null]

        """.trimIndent()

        val columns = ColumnParser.parseColumns(TABLE_NAME, columnsInput)

        assertEquals(true, columns.first().primaryKey)
        assertEquals(true, columns.first().notNull)
        assertEquals(true, columns.first().increment)
    }

    @Test
    fun `parses auto increment`() {
        val columnsInput = """
            age int [increment]

        """.trimIndent()

        val columns = ColumnParser.parseColumns(TABLE_NAME, columnsInput)

        assertEquals(true, columns.first().increment)
    }

    @Test
    fun `ignores column notes`() {
        val columnsInput = """
            age int [increment, note: 'overage above 18']

        """.trimIndent()

        val columns = ColumnParser.parseColumns(TABLE_NAME, columnsInput)

        assertEquals("age", columns.first().name)
        assertEquals("int", columns.first().type)
        assertEquals(true, columns.first().increment)
    }

    @Test
    fun `ignores multiple spaces after properties`() {
        val spaces = "      "
        val columnsInput = """
            age int [increment]$spaces

        """.trimIndent()

        val columns = ColumnParser.parseColumns(TABLE_NAME, columnsInput)

        assertEquals("age", columns.first().name)
        assertEquals("int", columns.first().type)
        assertEquals(true, columns.first().increment)
    }

    @Test
    fun `parses multiple columns`() {
        val columnsInput = """
            id int [pk, increment, not null]
            urn varchar [note: 'not null']
            monetizable int [default: `0`, note: 'nullable']
  
        """.trimIndent()

        val columns = ColumnParser.parseColumns(TABLE_NAME, columnsInput)

        assertEquals(3, columns.size)
    }
}