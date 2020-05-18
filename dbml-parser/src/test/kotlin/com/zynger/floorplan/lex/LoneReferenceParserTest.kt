package com.zynger.floorplan.lex

import com.zynger.floorplan.Reference
import com.zynger.floorplan.ReferenceOrder
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
        val referenceRawValue = "ref: posts.id - tags.post_id"
        val input = """
            Table posts {
              id int [pk, increment]
            }
            
            Table tags {
              id int [pk, increment] 
              post_id int 
            }
            
            $referenceRawValue
        """.trimIndent()
        val references = LoneReferenceParser.parseReferences(input)

        assertEquals(
            listOf(
                Reference(
                    referenceRawValue,
                    "posts",
                    "id",
                    "tags",
                    "post_id",
                    ReferenceOrder.OneToOne
                )
            ),
            references
        )
    }

    @Test
    fun `parses single lone reference with quotes surronding names`() {
        val referenceRawValue = "Ref: \"posts\".id - tags.\"post_id\""
        val input = """
            Table posts {
              id int [pk, increment]
            }
            
            Table tags {
              id int [pk, increment] 
              post_id int 
            }
            
            $referenceRawValue
        """.trimIndent()
        val references = LoneReferenceParser.parseReferences(input)

        assertEquals(
            listOf(
                Reference(
                    referenceRawValue,
                    "posts",
                    "id",
                    "tags",
                    "post_id",
                    ReferenceOrder.OneToOne
                )
            ),
            references
        )
    }

    @Test
    fun `parses single lone reference with one to one order`() {
        val referenceRawValue = "Ref: posts.id - tags.post_id"
        val input = """
            Table posts {
              id int [pk, increment]
            }
            
            Table tags {
              id int [pk, increment] 
              post_id int 
            }
            
            $referenceRawValue
        """.trimIndent()
        val references = LoneReferenceParser.parseReferences(input)

        assertEquals(
            listOf(
                Reference(
                    referenceRawValue,
                    "posts",
                    "id",
                    "tags",
                    "post_id",
                    ReferenceOrder.OneToOne
                )
            ),
            references
        )
    }

    @Test
    fun `parses single lone reference with one to many order`() {
        val referenceRawValue = "Ref: posts.id < tags.post_id"
        val input = """
            Table posts {
              id int [pk, increment]
            }
            
            Table tags {
              id int [pk, increment] 
              post_id int 
            }
            
            $referenceRawValue
        """.trimIndent()
        val references = LoneReferenceParser.parseReferences(input)

        assertEquals(
            listOf(
                Reference(
                    referenceRawValue,
                    "posts",
                    "id",
                    "tags",
                    "post_id",
                    ReferenceOrder.OneToMany
                )
            ),
            references
        )
    }

    @Test
    fun `parses single lone reference with many to one order`() {
        val referenceRawValue = "Ref: posts.id > tags.post_id"
        val input = """
            Table posts {
              id int [pk, increment]
            }
            
            Table tags {
              id int [pk, increment] 
              post_id int 
            }
            
            $referenceRawValue
        """.trimIndent()
        val references = LoneReferenceParser.parseReferences(input)

        assertEquals(
            listOf(
                Reference(
                    referenceRawValue,
                    "posts",
                    "id",
                    "tags",
                    "post_id",
                    ReferenceOrder.ManyToOne
                )
            ),
            references
        )
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
                    "Ref: trending_shows.show_id - shows.id",
                    "trending_shows",
                    "show_id",
                    "shows",
                    "id",
                    ReferenceOrder.OneToOne
                )
            ),
            references
        )
    }
}