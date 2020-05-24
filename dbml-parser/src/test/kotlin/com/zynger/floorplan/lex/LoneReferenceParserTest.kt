package com.zynger.floorplan.lex

import com.zynger.floorplan.dbml.Reference
import com.zynger.floorplan.dbml.ReferenceOrder
import org.junit.Assert.assertEquals
import org.junit.Test

class LoneReferenceParserTest {

    @Test
    fun `when no references then empty list`() {
        val input = """
            Table users {
              id int [pk, increment]
            }
        """.trimIndent()
        val references = LoneReferenceParser.parseReferences(input)

        assertEquals(
            emptyList<Reference>(),
            references
        )
    }

    @Test
    fun `parses single lone reference with lowercase ref`() {
        val input = """
            Table posts {
              id int [pk, increment]
            }
            
            Table tags {
              id int [pk, increment] 
              post_id int 
            }
            
            ref: posts.id - tags.post_id
        """.trimIndent()
        val references = LoneReferenceParser.parseReferences(input)

        assertEquals(1, references.size)
        val reference = references.first()
        assertEquals("posts", reference.fromTable)
        assertEquals("id", reference.fromColumn)
        assertEquals("tags", reference.toTable)
        assertEquals("post_id", reference.toColumn)
        assertEquals(ReferenceOrder.OneToOne, reference.referenceOrder)
    }

    @Test
    fun `parses single lone reference with quotes surronding names`() {
        val input = """
            Table posts {
              id int [pk, increment]
            }
            
            Table tags {
              id int [pk, increment] 
              post_id int 
            }
            
            Ref: "posts".id - tags."post_id"
        """.trimIndent()
        val references = LoneReferenceParser.parseReferences(input)

        assertEquals(1, references.size)
        val reference = references.first()
        assertEquals("posts", reference.fromTable)
        assertEquals("id", reference.fromColumn)
        assertEquals("tags", reference.toTable)
        assertEquals("post_id", reference.toColumn)
        assertEquals(ReferenceOrder.OneToOne, reference.referenceOrder)
    }

    @Test
    fun `parses single lone reference with one to one order`() {
        val input = """
            Table posts {
              id int [pk, increment]
            }
            
            Table tags {
              id int [pk, increment] 
              post_id int 
            }
            
            Ref: posts.id - tags.post_id
        """.trimIndent()

        val references = LoneReferenceParser.parseReferences(input)

        assertEquals(1, references.size)
        val reference = references.first()
        assertEquals(ReferenceOrder.OneToOne, reference.referenceOrder)
    }

    @Test
    fun `parses single lone reference with one to many order`() {
        val input = """
            Table posts {
              id int [pk, increment]
            }
            
            Table tags {
              id int [pk, increment] 
              post_id int 
            }
            
            Ref: posts.id < tags.post_id
        """.trimIndent()

        val references = LoneReferenceParser.parseReferences(input)

        assertEquals(1, references.size)
        val reference = references.first()
        assertEquals(ReferenceOrder.OneToMany, reference.referenceOrder)
    }

    @Test
    fun `parses single lone reference with many to one order`() {
        val input = """
            Table posts {
              id int [pk, increment]
            }
            
            Table tags {
              id int [pk, increment] 
              post_id int 
            }
            
            Ref: posts.id > tags.post_id
        """.trimIndent()

        val references = LoneReferenceParser.parseReferences(input)

        assertEquals(1, references.size)
        val reference = references.first()
        assertEquals(ReferenceOrder.ManyToOne, reference.referenceOrder)
    }

    @Test
    fun `parses multiple lone references`() {
        val input = """
            Table posts {
              id int [pk, increment]
            }
            
            Table tags {
              id int [pk, increment] 
              post_id int 
            }
            
            Table users {
              id int [pk, increment]
            }
            
            Ref: posts.id - tags.post_id
            Ref: users.id - posts.id
        """.trimIndent()
        val references = LoneReferenceParser.parseReferences(input)

        assertEquals(2, references.size)
    }

    @Test
    fun `ignores reference notes`() {
        val input = "Ref: trending_shows.show_id - shows.id [delete: cascade, update: cascade]"
        val references = LoneReferenceParser.parseReferences(input)

        assertEquals(
            listOf(
                Reference(
                    rawValue = "Ref: trending_shows.show_id - shows.id",
                    fromTable = "trending_shows",
                    fromColumn = "show_id",
                    toTable = "shows",
                    toColumn = "id",
                    referenceOrder = ReferenceOrder.OneToOne
                )
            ),
            references
        )
    }
}