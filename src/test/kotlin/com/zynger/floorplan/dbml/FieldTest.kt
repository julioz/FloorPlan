package com.zynger.floorplan.dbml

import com.zynger.floorplan.model.PrimaryKey
import org.junit.Assert.assertEquals
import com.zynger.floorplan.model.Field as DbField
import org.junit.Test

class FieldTest {
    companion object {
        private const val FIELD_PATH = "fieldPath"
        private const val COLUMN_NAME = "username"
    }

    @Test
    fun `non-primary key with TEXT type becomes VARCHAR with nullability note`() {
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "TEXT", false)
        val tablePrimaryKey = PrimaryKey(emptyList(), false)

        assertEquals(
            """
                $COLUMN_NAME varchar [note: 'nullable']
            """.trimIndent(),
            Field(textField, tablePrimaryKey).toString()
        )
    }

    @Test
    fun `non-primary key with TEXT type becomes VARCHAR with nonnull note`() {
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "TEXT", true)
        val tablePrimaryKey = PrimaryKey(emptyList(), false)

        assertEquals(
            """
                $COLUMN_NAME varchar [note: 'not null']
            """.trimIndent(),
            Field(textField, tablePrimaryKey).toString()
        )
    }

    @Test
    fun `primary key with INTEGER type becomes int`() {
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "INTEGER", false)
        val tablePrimaryKey = PrimaryKey(listOf(COLUMN_NAME), false)

        assertEquals(
            """
                $COLUMN_NAME int [pk]
            """.trimIndent(),
            Field(textField, tablePrimaryKey).toString()
        )
    }

    @Test
    fun `primary key with TEXT type becomes VARCHAR`() {
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "TEXT", false)
        val tablePrimaryKey = PrimaryKey(listOf(COLUMN_NAME), false)

        assertEquals(
            """
                $COLUMN_NAME varchar [pk]
            """.trimIndent(),
            Field(textField, tablePrimaryKey).toString()
        )
    }

    @Test
    fun `autoincrement primary key`() {
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "TEXT", false)
        val tablePrimaryKey = PrimaryKey(listOf(COLUMN_NAME), true)

        assertEquals(
            """
                $COLUMN_NAME varchar [pk, increment]
            """.trimIndent(),
            Field(textField, tablePrimaryKey).toString()
        )
    }
}