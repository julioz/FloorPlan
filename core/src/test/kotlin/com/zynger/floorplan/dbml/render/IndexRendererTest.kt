package com.zynger.floorplan.dbml.render

import com.zynger.floorplan.dbml.Index
import org.junit.Assert.assertEquals
import org.junit.Test

class IndexRendererTest {

    companion object {
        private const val INDEX_NAME = "aRandomIndex"
        private const val COLUMN_NAME = "username"
    }

    @Test
    fun `list single column index`() {
        val index = Index(
            INDEX_NAME,
            unique = false,
            columnNames = listOf(COLUMN_NAME)
        )

        assertEquals(
            """
                ($COLUMN_NAME) [name:'$INDEX_NAME']
            """.trimIndent(),
            IndexRenderer(index).render()
        )
    }

    @Test
    fun `lists multiple column index`() {
        val column2 = "col2"
        val column3 = "col3"
        val index = Index(
            INDEX_NAME,
            unique = false,
            columnNames = listOf(COLUMN_NAME, column2, column3)
        )

        assertEquals(
            """
                ($COLUMN_NAME,$column2,$column3) [name:'$INDEX_NAME']
            """.trimIndent(),
            IndexRenderer(index).render()
        )
    }

    @Test
    fun `adds uniqueness note when index is unique`() {
        val index = Index(
            INDEX_NAME,
            unique = true,
            columnNames = listOf(COLUMN_NAME)
        )

        assertEquals(
            """
                ($COLUMN_NAME) [name:'$INDEX_NAME', unique]
            """.trimIndent(),
            IndexRenderer(index).render()
        )
    }
}
