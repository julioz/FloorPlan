package com.zynger.floorplan.lex

import com.zynger.floorplan.Reference
import com.zynger.floorplan.ReferenceOrder
import org.junit.Assert.assertEquals
import org.junit.Test

class ColumnReferenceParserTest {

    companion object {
        private const val TABLE_NAME = "myTable"
        private const val FROM_COLUMN_NAME = "myColumn"
    }

    @Test
    fun `empty column notes` () {
        val columnProperties = ""
        val reference: Reference? = ColumnReferenceParser.parse(TABLE_NAME, FROM_COLUMN_NAME, columnProperties)
        assertEquals(null, reference)
    }

    @Test
    fun `no reference column properties` () {
        val columnProperties = "[note: 'not null']"
        val reference: Reference? = ColumnReferenceParser.parse(TABLE_NAME, FROM_COLUMN_NAME, columnProperties)
        assertEquals(null, reference)
    }

    @Test
    fun `irrelevant column properties` () {
        val columnProperties = "[pk, increment, note: 'ref: something else']"
        val reference: Reference? = ColumnReferenceParser.parse(TABLE_NAME, FROM_COLUMN_NAME, columnProperties)
        assertEquals(null, reference)
    }

    @Test
    fun `parses reference`() {
        val columnProperties = "post_id int [not null, Ref: - posts.id]"
        val reference: Reference? = ColumnReferenceParser.parse(TABLE_NAME, FROM_COLUMN_NAME, columnProperties)

        reference!!
        assertEquals(TABLE_NAME, reference.fromTable)
        assertEquals(FROM_COLUMN_NAME, reference.fromColumn)
        assertEquals("posts", reference.toTable)
        assertEquals("id", reference.toColumn)
        assertEquals(ReferenceOrder.OneToOne, reference.referenceOrder)
    }

    @Test
    fun `parses reference with quotes surronding names`() {
        val columnProperties = "post_id int [not null, ref: > \"posts\".\"id\"]"
        val reference: Reference? = ColumnReferenceParser.parse(TABLE_NAME, FROM_COLUMN_NAME, columnProperties)

        reference!!
        assertEquals(TABLE_NAME, reference.fromTable)
        assertEquals(FROM_COLUMN_NAME, reference.fromColumn)
        assertEquals("posts", reference.toTable)
        assertEquals("id", reference.toColumn)
        assertEquals(ReferenceOrder.ManyToOne, reference.referenceOrder)
    }
}
