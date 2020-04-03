package com.zynger.floorplan.dbml

import com.zynger.floorplan.model.ForeignKey
import org.junit.Assert
import org.junit.Test

class ReferenceTest {

    companion object {
        private const val FROM_TABLE = "referrer"
        private const val TO_TABLE = "referenced"
        private const val FROM_COLUMN = "userId"
        private const val TO_COLUMN = "id"
        private const val GARBLE = "egal"
        private const val ON_DELETE = GARBLE
        private const val ON_UPDATE = GARBLE
    }

    @Test
    fun `references from 1 column to 1 column`() {
        val reference = Reference(FROM_TABLE,
            ForeignKey(TO_TABLE, listOf(FROM_COLUMN), listOf(TO_COLUMN), ON_DELETE, ON_UPDATE)
        )

        Assert.assertEquals(
            """
                Ref: $FROM_TABLE.$FROM_COLUMN - $TO_TABLE.$TO_COLUMN
            """.trimIndent(),
            reference.toString()
        )
    }

    @Test
    fun `only pick the first referenced column`() {
        val reference = Reference(FROM_TABLE,
            ForeignKey(TO_TABLE, listOf(FROM_COLUMN, "another_col"), listOf(TO_COLUMN, "col2"), ON_DELETE, ON_UPDATE)
        )

        Assert.assertEquals(
            """
                Ref: $FROM_TABLE.$FROM_COLUMN - $TO_TABLE.$TO_COLUMN
            """.trimIndent(),
            reference.toString()
        )
    }
}