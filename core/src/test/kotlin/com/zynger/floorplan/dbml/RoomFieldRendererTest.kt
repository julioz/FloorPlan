package com.zynger.floorplan.dbml

import com.zynger.floorplan.Settings
import com.zynger.floorplan.room.PrimaryKey
import org.junit.Assert.assertEquals
import com.zynger.floorplan.room.Field as DbField
import org.junit.Test

class RoomFieldRendererTest {
    companion object {
        private const val FIELD_PATH = "fieldPath"
        private const val COLUMN_NAME = "username"
        private val DEFAULT_SETTINGS = Settings()
    }

    @Test
    fun `non-primary key with TEXT type becomes VARCHAR with nullability note`() {
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "TEXT", false)
        val tablePrimaryKey = PrimaryKey(emptyList(), false)

        assertEquals(
            """
                $COLUMN_NAME varchar [note: 'nullable']
            """.trimIndent(),
            RoomFieldRenderer(textField, tablePrimaryKey, DEFAULT_SETTINGS).toString()
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
            RoomFieldRenderer(textField, tablePrimaryKey, DEFAULT_SETTINGS).toString()
        )
    }

    @Test
    fun `non-primary key with default value gets rendered with default note`() {
        val defaultValue = "defVal"
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "TEXT", true, defaultValue)
        val tablePrimaryKey = PrimaryKey(emptyList(), false)

        assertEquals(
            """
                $COLUMN_NAME varchar [default: `$defaultValue`, note: 'not null']
            """.trimIndent(),
            RoomFieldRenderer(textField, tablePrimaryKey, DEFAULT_SETTINGS).toString()
        )
    }

    @Test
    fun `non-primary key with default value gets rendered with default note even if it is an empty string`() {
        val defaultValue = ""
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "TEXT", true, defaultValue)
        val tablePrimaryKey = PrimaryKey(emptyList(), false)

        assertEquals(
            """
                $COLUMN_NAME varchar [default: ``, note: 'not null']
            """.trimIndent(),
            RoomFieldRenderer(textField, tablePrimaryKey, DEFAULT_SETTINGS).toString()
        )
    }

    @Test
    fun `primary key with default value gets rendered with default note`() {
        val defaultValue = "defVal"
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "INTEGER", false, defaultValue)
        val tablePrimaryKey = PrimaryKey(listOf(COLUMN_NAME), false)

        assertEquals(
            """
                $COLUMN_NAME int [default: `$defaultValue`, pk]
            """.trimIndent(),
            RoomFieldRenderer(textField, tablePrimaryKey, DEFAULT_SETTINGS).toString()
        )
    }

    @Test
    fun `non-primary key with default value using quotes gets rendered with default note`() {
        val defaultValue = "\"quoted\""
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "TEXT", true, defaultValue)
        val tablePrimaryKey = PrimaryKey(emptyList(), false)

        assertEquals(
            """
                $COLUMN_NAME varchar [default: `$defaultValue`, note: 'not null']
            """.trimIndent(),
            RoomFieldRenderer(textField, tablePrimaryKey, DEFAULT_SETTINGS).toString()
        )
    }

    @Test
    fun `field with INTEGER type becomes int`() {
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "INTEGER", false)
        val tablePrimaryKey = PrimaryKey(listOf(COLUMN_NAME), false)

        assertEquals(
            """
                $COLUMN_NAME int [pk]
            """.trimIndent(),
            RoomFieldRenderer(textField, tablePrimaryKey, DEFAULT_SETTINGS).toString()
        )
    }

    @Test
    fun `field with TEXT type becomes VARCHAR`() {
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "TEXT", false)
        val tablePrimaryKey = PrimaryKey(listOf(COLUMN_NAME), false)

        assertEquals(
            """
                $COLUMN_NAME varchar [pk]
            """.trimIndent(),
            RoomFieldRenderer(textField, tablePrimaryKey, DEFAULT_SETTINGS).toString()
        )
    }

    @Test
    fun `field with REAL type becomes REAL`() {
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "REAL", false)
        val tablePrimaryKey = PrimaryKey(listOf(COLUMN_NAME), false)

        assertEquals(
            """
                $COLUMN_NAME real [pk]
            """.trimIndent(),
            RoomFieldRenderer(textField, tablePrimaryKey, DEFAULT_SETTINGS).toString()
        )
    }

    @Test
    fun `field with BLOB type becomes BLOB`() {
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "BLOB", false)
        val tablePrimaryKey = PrimaryKey(listOf(COLUMN_NAME), false)

        assertEquals(
            """
                $COLUMN_NAME blob [pk]
            """.trimIndent(),
            RoomFieldRenderer(textField, tablePrimaryKey, DEFAULT_SETTINGS).toString()
        )
    }

    @Test
    fun `field with unrecognized type follows DBML single-word requirement`() {
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "Decimal (1, 2)", false)
        val tablePrimaryKey = PrimaryKey(listOf(COLUMN_NAME), false)

        assertEquals(
            """
                $COLUMN_NAME decimal(1,2) [pk]
            """.trimIndent(),
            RoomFieldRenderer(textField, tablePrimaryKey, DEFAULT_SETTINGS).toString()
        )
    }

    @Test
    fun `field with INTEGER type becomes int(?) when nullable should be rendered`() {
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "INTEGER", false)
        val tablePrimaryKey = PrimaryKey(listOf(COLUMN_NAME), false)

        assertEquals(
            """
                $COLUMN_NAME int(?) [pk]
            """.trimIndent(),
            RoomFieldRenderer(textField, tablePrimaryKey, Settings(renderNullableFields = true)).toString()
        )
    }

    @Test
    fun `field with unrecognized type follows DBML single-word requirement even when nullable should be rendered`() {
        val textField = DbField(FIELD_PATH, COLUMN_NAME, "Decimal (1, 2)", false)
        val tablePrimaryKey = PrimaryKey(listOf(COLUMN_NAME), false)

        assertEquals(
            """
                $COLUMN_NAME decimal(1,2)(?) [pk]
            """.trimIndent(),
            RoomFieldRenderer(textField, tablePrimaryKey, Settings(renderNullableFields = true)).toString()
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
            RoomFieldRenderer(textField, tablePrimaryKey, DEFAULT_SETTINGS).toString()
        )
    }
}