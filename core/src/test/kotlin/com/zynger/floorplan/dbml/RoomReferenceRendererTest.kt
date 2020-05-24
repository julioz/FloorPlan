package com.zynger.floorplan.dbml

import com.zynger.floorplan.room.ForeignKey
import com.zynger.floorplan.room.ForeignKeyAction
import org.junit.Assert
import org.junit.Test

class RoomReferenceRendererTest {

    companion object {
        private const val FROM_TABLE = "referrer"
        private const val TO_TABLE = "referenced"
        private const val FROM_COLUMN = "userId"
        private const val TO_COLUMN = "id"
        private val ON_DELETE = ForeignKeyAction.NO_ACTION
        private val ON_UPDATE = ForeignKeyAction.NO_ACTION
    }

    @Test
    fun `references from 1 column to 1 column`() {
        val reference = RoomReferenceRenderer(FROM_TABLE,
            ForeignKey(TO_TABLE, listOf(FROM_COLUMN), listOf(TO_COLUMN), ON_DELETE, ON_UPDATE)
        )

        Assert.assertEquals(
            """
                Ref: $FROM_TABLE.$FROM_COLUMN - $TO_TABLE.$TO_COLUMN [delete: no action, update: no action]
            """.trimIndent(),
            reference.toString()
        )
    }

    @Test
    fun `only pick the first referenced column`() {
        val reference = RoomReferenceRenderer(FROM_TABLE,
            ForeignKey(TO_TABLE, listOf(FROM_COLUMN, "another_col"), listOf(TO_COLUMN, "col2"), ON_DELETE, ON_UPDATE)
        )

        Assert.assertEquals(
            """
                Ref: $FROM_TABLE.$FROM_COLUMN - $TO_TABLE.$TO_COLUMN [delete: no action, update: no action]
            """.trimIndent(),
            reference.toString()
        )
    }

    @Test
    fun `foreign key action gets translated for RESTRICT action`() {
        val reference = RoomReferenceRenderer(FROM_TABLE,
            ForeignKey(TO_TABLE, listOf(FROM_COLUMN), listOf(TO_COLUMN), ForeignKeyAction.RESTRICT, ForeignKeyAction.RESTRICT)
        )

        Assert.assertEquals(
            """
                Ref: $FROM_TABLE.$FROM_COLUMN - $TO_TABLE.$TO_COLUMN [delete: restrict, update: restrict]
            """.trimIndent(),
            reference.toString()
        )
    }

    @Test
    fun `foreign key action gets translated for SET NULL action`() {
        val reference = RoomReferenceRenderer(FROM_TABLE,
            ForeignKey(TO_TABLE, listOf(FROM_COLUMN), listOf(TO_COLUMN), ForeignKeyAction.SET_NULL, ForeignKeyAction.SET_NULL)
        )

        Assert.assertEquals(
            """
                Ref: $FROM_TABLE.$FROM_COLUMN - $TO_TABLE.$TO_COLUMN [delete: set null, update: set null]
            """.trimIndent(),
            reference.toString()
        )
    }

    @Test
    fun `foreign key action gets translated for SET DEFAULT action`() {
        val reference = RoomReferenceRenderer(FROM_TABLE,
            ForeignKey(TO_TABLE, listOf(FROM_COLUMN), listOf(TO_COLUMN), ForeignKeyAction.SET_DEFAULT, ForeignKeyAction.SET_DEFAULT)
        )

        Assert.assertEquals(
            """
                Ref: $FROM_TABLE.$FROM_COLUMN - $TO_TABLE.$TO_COLUMN [delete: set default, update: set default]
            """.trimIndent(),
            reference.toString()
        )
    }

    @Test
    fun `foreign key action gets translated for CASCADE action`() {
        val reference = RoomReferenceRenderer(FROM_TABLE,
            ForeignKey(TO_TABLE, listOf(FROM_COLUMN), listOf(TO_COLUMN), ForeignKeyAction.CASCADE, ForeignKeyAction.CASCADE)
        )

        Assert.assertEquals(
            """
                Ref: $FROM_TABLE.$FROM_COLUMN - $TO_TABLE.$TO_COLUMN [delete: cascade, update: cascade]
            """.trimIndent(),
            reference.toString()
        )
    }
}