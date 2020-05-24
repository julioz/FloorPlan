package com.zynger.floorplan.dbml.render

import com.zynger.floorplan.dbml.Reference
import com.zynger.floorplan.dbml.ReferenceOrder
import org.junit.Assert.assertEquals
import org.junit.Test

class ReferenceRendererTest {

    companion object {
        private const val FROM_TABLE = "referrer"
        private const val TO_TABLE = "referenced"
        private const val FROM_COLUMN = "userId"
        private const val TO_COLUMN = "id"
        private const val ON_DELETE = "no action"
        private const val ON_UPDATE = "no action"
    }

    @Test
    fun `references from 1 column to 1 column`() {
        val referenceRenderer = ReferenceRenderer(
            Reference(
                FROM_TABLE,
                FROM_COLUMN,
                TO_TABLE,
                TO_COLUMN,
                ReferenceOrder.OneToOne,
                ON_UPDATE,
                ON_DELETE
            )
        )

        assertEquals(
            """
                Ref: $FROM_TABLE.$FROM_COLUMN - $TO_TABLE.$TO_COLUMN [delete: no action, update: no action]
            """.trimIndent(),
            referenceRenderer.render()
        )
    }

    @Test
    fun `references from 1 column to many column`() {
        val referenceRenderer = ReferenceRenderer(
            Reference(
                FROM_TABLE,
                FROM_COLUMN,
                TO_TABLE,
                TO_COLUMN,
                ReferenceOrder.OneToMany,
                ON_UPDATE,
                ON_DELETE
            )
        )

        assertEquals(
            """
                Ref: $FROM_TABLE.$FROM_COLUMN < $TO_TABLE.$TO_COLUMN [delete: no action, update: no action]
            """.trimIndent(),
            referenceRenderer.render()
        )
    }

    @Test
    fun `references from many column to one column`() {
        val referenceRenderer = ReferenceRenderer(
            Reference(
                FROM_TABLE,
                FROM_COLUMN,
                TO_TABLE,
                TO_COLUMN,
                ReferenceOrder.ManyToOne,
                ON_UPDATE,
                ON_DELETE
            )
        )

        assertEquals(
            """
                Ref: $FROM_TABLE.$FROM_COLUMN > $TO_TABLE.$TO_COLUMN [delete: no action, update: no action]
            """.trimIndent(),
            referenceRenderer.render()
        )
    }

    @Test
    fun `foreign key action gets translated for RESTRICT action`() {
        val referenceRenderer = ReferenceRenderer(
            Reference(
                FROM_TABLE,
                FROM_COLUMN,
                TO_TABLE,
                TO_COLUMN,
                ReferenceOrder.OneToOne,
                "restrict",
                "restrict"
            )
        )

        assertEquals(
            """
                Ref: $FROM_TABLE.$FROM_COLUMN - $TO_TABLE.$TO_COLUMN [delete: restrict, update: restrict]
            """.trimIndent(),
            referenceRenderer.render()
        )
    }

    @Test
    fun `foreign key action gets translated for SET NULL action`() {
        val referenceRenderer = ReferenceRenderer(
            Reference(
                FROM_TABLE,
                FROM_COLUMN,
                TO_TABLE,
                TO_COLUMN,
                ReferenceOrder.OneToOne,
                "set null",
                "set null"
            )
        )

        assertEquals(
            """
                Ref: $FROM_TABLE.$FROM_COLUMN - $TO_TABLE.$TO_COLUMN [delete: set null, update: set null]
            """.trimIndent(),
            referenceRenderer.render()
        )
    }

    @Test
    fun `foreign key action gets translated for SET DEFAULT action`() {
        val referenceRenderer = ReferenceRenderer(
            Reference(
                FROM_TABLE,
                FROM_COLUMN,
                TO_TABLE,
                TO_COLUMN,
                ReferenceOrder.OneToOne,
                "set default",
                "set default"
            )
        )

        assertEquals(
            """
                Ref: $FROM_TABLE.$FROM_COLUMN - $TO_TABLE.$TO_COLUMN [delete: set default, update: set default]
            """.trimIndent(),
            referenceRenderer.render()
        )
    }

    @Test
    fun `foreign key action gets translated for CASCADE action`() {
        val referenceRenderer = ReferenceRenderer(
            Reference(
                FROM_TABLE,
                FROM_COLUMN,
                TO_TABLE,
                TO_COLUMN,
                ReferenceOrder.OneToOne,
                "cascade",
                "cascade"
            )
        )

        assertEquals(
            """
                Ref: $FROM_TABLE.$FROM_COLUMN - $TO_TABLE.$TO_COLUMN [delete: cascade, update: cascade]
            """.trimIndent(),
            referenceRenderer.render()
        )
    }
}
