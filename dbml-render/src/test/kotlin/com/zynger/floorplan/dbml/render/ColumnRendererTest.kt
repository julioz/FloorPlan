package com.zynger.floorplan.dbml.render

import com.zynger.floorplan.Settings
import com.zynger.floorplan.dbml.Column
import org.junit.Assert.assertEquals
import org.junit.Test

class ColumnRendererTest {
    companion object {
        private const val FIELD_PATH = "fieldPath"
        private const val COLUMN_NAME = "username"
        private val DEFAULT_SETTINGS = Settings()
    }

    @Test
    fun `non-primary key with TEXT type becomes VARCHAR with nullability note`() {
        val textField = Column(
            rawValue = FIELD_PATH,
            name = COLUMN_NAME,
            type = "TEXT",
            notNull = false,
            primaryKey = false
        )

        assertEquals(
            """
                $COLUMN_NAME varchar [note: 'nullable']
            """.trimIndent(),
            ColumnRenderer(
                textField,
                DEFAULT_SETTINGS
            ).render()
        )
    }

    @Test
    fun `non-primary key with TEXT type becomes VARCHAR with nonnull note`() {
        val textField = Column(
            rawValue = FIELD_PATH,
            name = COLUMN_NAME,
            type = "TEXT",
            notNull = true,
            primaryKey = false
        )

        assertEquals(
            """
                $COLUMN_NAME varchar [note: 'not null']
            """.trimIndent(),
            ColumnRenderer(
                textField,
                DEFAULT_SETTINGS
            ).render()
        )
    }

    @Test
    fun `non-primary key with default value gets rendered with default note`() {
        val defaultValue = "defVal"
        val textField = Column(
            rawValue = FIELD_PATH,
            name = COLUMN_NAME,
            type = "TEXT",
            notNull = true,
            defaultValue = defaultValue,
            primaryKey = false
        )

        assertEquals(
            """
                $COLUMN_NAME varchar [default: `$defaultValue`, note: 'not null']
            """.trimIndent(),
            ColumnRenderer(
                textField,
                DEFAULT_SETTINGS
            ).render()
        )
    }

    @Test
    fun `non-primary key with default value gets rendered with default note even if it is an empty string`() {
        val defaultValue = ""
        val textField = Column(
            rawValue = FIELD_PATH,
            name = COLUMN_NAME,
            type = "TEXT",
            notNull = true,
            defaultValue = defaultValue,
            primaryKey = false
        )

        assertEquals(
            """
                $COLUMN_NAME varchar [default: ``, note: 'not null']
            """.trimIndent(),
            ColumnRenderer(
                textField,
                DEFAULT_SETTINGS
            ).render()
        )
    }

    @Test
    fun `primary key with default value gets rendered with default note`() {
        val defaultValue = "defVal"
        val textField = Column(
            rawValue = FIELD_PATH,
            name = COLUMN_NAME,
            type = "INTEGER",
            notNull = false,
            defaultValue = defaultValue,
            primaryKey = true,
            increment = false
        )

        assertEquals(
            """
                $COLUMN_NAME int [default: `$defaultValue`, pk]
            """.trimIndent(),
            ColumnRenderer(
                textField,
                DEFAULT_SETTINGS
            ).render()
        )
    }

    @Test
    fun `non-primary key with default value using quotes gets rendered with default note`() {
        val defaultValue = "\"quoted\""
        val textField = Column(
            rawValue = FIELD_PATH,
            name = COLUMN_NAME,
            type = "TEXT",
            notNull = true,
            defaultValue = defaultValue,
            primaryKey = false
        )

        assertEquals(
            """
                $COLUMN_NAME varchar [default: `$defaultValue`, note: 'not null']
            """.trimIndent(),
            ColumnRenderer(
                textField,
                DEFAULT_SETTINGS
            ).render()
        )
    }

    @Test
    fun `field with INTEGER type becomes int`() {
        val textField = Column(
            rawValue = FIELD_PATH,
            name = COLUMN_NAME,
            type = "INTEGER",
            notNull = false,
            primaryKey = true,
            increment = false
        )

        assertEquals(
            """
                $COLUMN_NAME int [pk]
            """.trimIndent(),
            ColumnRenderer(
                textField,
                DEFAULT_SETTINGS
            ).render()
        )
    }

    @Test
    fun `field with TEXT type becomes VARCHAR`() {
        val textField = Column(
            rawValue = FIELD_PATH,
            name = COLUMN_NAME,
            type = "TEXT",
            notNull = false,
            primaryKey = true,
            increment = false
        )

        assertEquals(
            """
                $COLUMN_NAME varchar [pk]
            """.trimIndent(),
            ColumnRenderer(
                textField,
                DEFAULT_SETTINGS
            ).render()
        )
    }

    @Test
    fun `field with REAL type becomes REAL`() {
        val textField = Column(
            rawValue = FIELD_PATH,
            name = COLUMN_NAME,
            type = "REAL",
            notNull = false,
            primaryKey = true,
            increment = false
        )

        assertEquals(
            """
                $COLUMN_NAME real [pk]
            """.trimIndent(),
            ColumnRenderer(
                textField,
                DEFAULT_SETTINGS
            ).render()
        )
    }

    @Test
    fun `field with BLOB type becomes BLOB`() {
        val textField = Column(
            rawValue = FIELD_PATH,
            name = COLUMN_NAME,
            type = "BLOB",
            notNull = false,
            primaryKey = true,
            increment = false
        )

        assertEquals(
            """
                $COLUMN_NAME blob [pk]
            """.trimIndent(),
            ColumnRenderer(
                textField,
                DEFAULT_SETTINGS
            ).render()
        )
    }

    @Test
    fun `field with unrecognized type follows DBML single-word requirement`() {
        val textField = Column(
            rawValue = FIELD_PATH,
            name = COLUMN_NAME,
            type = "Decimal (1, 2)",
            notNull = false,
            primaryKey = true,
            increment = false
        )

        assertEquals(
            """
                $COLUMN_NAME decimal(1,2) [pk]
            """.trimIndent(),
            ColumnRenderer(
                textField,
                DEFAULT_SETTINGS
            ).render()
        )
    }

    @Test
    fun `field with INTEGER type becomes int(?) when nullable should be rendered`() {
        val textField = Column(
            rawValue = FIELD_PATH,
            name = COLUMN_NAME,
            type = "INTEGER",
            notNull = false,
            primaryKey = true,
            increment = false
        )

        assertEquals(
            """
                $COLUMN_NAME int(?) [pk]
            """.trimIndent(),
            ColumnRenderer(
                textField,
                Settings(renderNullableFields = true)
            ).render()
        )
    }

    @Test
    fun `field with unrecognized type follows DBML single-word requirement even when nullable should be rendered`() {
        val textField = Column(
            rawValue = FIELD_PATH,
            name = COLUMN_NAME,
            type = "Decimal (1, 2)",
            notNull = false,
            primaryKey = true,
            increment = false
        )

        assertEquals(
            """
                $COLUMN_NAME decimal(1,2)(?) [pk]
            """.trimIndent(),
            ColumnRenderer(
                textField,
                Settings(renderNullableFields = true)
            ).render()
        )
    }

    @Test
    fun `autoincrement primary key`() {
        val textField = Column(
            rawValue = FIELD_PATH,
            name = COLUMN_NAME,
            type = "TEXT",
            notNull = false,
            primaryKey = true,
            increment = true
        )

        assertEquals(
            """
                $COLUMN_NAME varchar [pk, increment]
            """.trimIndent(),
            ColumnRenderer(
                textField,
                DEFAULT_SETTINGS
            ).render()
        )
    }
}