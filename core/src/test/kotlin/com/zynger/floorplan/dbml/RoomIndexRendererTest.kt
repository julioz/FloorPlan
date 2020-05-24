package com.zynger.floorplan.dbml

import com.zynger.floorplan.room.Index as DbIndex
import org.junit.Assert
import org.junit.Test

class RoomIndexRendererTest {

    companion object {
        private const val INDEX_NAME = "aRandomIndex"
        private const val COLUMN_NAME = "username"
        private const val CREATE_SQL = "SELECT * FROM MyTable"
    }

    @Test
    fun `list single column index`() {
        val index = DbIndex(
            INDEX_NAME,
            unique = false,
            columnNames = listOf(COLUMN_NAME),
            createSql = CREATE_SQL
        )

        Assert.assertEquals(
            """
                ($COLUMN_NAME) [name:'$INDEX_NAME']
            """.trimIndent(),
            RoomIndexRenderer(index).toString()
        )
    }

    @Test
    fun `lists multiple column index`() {
        val column2 = "col2"
        val column3 = "col3"
        val index = DbIndex(
            INDEX_NAME,
            unique = false,
            columnNames = listOf(COLUMN_NAME, column2, column3),
            createSql = CREATE_SQL
        )

        Assert.assertEquals(
            """
                ($COLUMN_NAME,$column2,$column3) [name:'$INDEX_NAME']
            """.trimIndent(),
            RoomIndexRenderer(index).toString()
        )
    }

    @Test
    fun `adds uniqueness note when index is unique`() {
        val index = DbIndex(
            INDEX_NAME,
            unique = true,
            columnNames = listOf(COLUMN_NAME),
            createSql = CREATE_SQL
        )

        Assert.assertEquals(
            """
                ($COLUMN_NAME) [name:'$INDEX_NAME', unique]
            """.trimIndent(),
            RoomIndexRenderer(index).toString()
        )
    }
}