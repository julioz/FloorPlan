package com.zynger.floorplan.lex

import org.junit.Assert.*
import org.junit.Test

class IndexParserTest {
    @Test
    fun `no indexes`() {
        val input = """
          id int [pk]
          post_id int
          tag_id int
        """.trimIndent()

        val indexes = IndexParser.parseIndexes(input)

        assertEquals(0, indexes.size)
    }

    @Test
    fun `no indexes in block`() {
        val input = """
          id int [pk]
          post_id int
          tag_id int
        
          Indexes  {
          }
        """.trimIndent()

        val indexes = IndexParser.parseIndexes(input)

        assertEquals(0, indexes.size)
    }

    @Test
    fun `index with no explicit name`() {
        val input = """
          id int [pk]
          post_id int
          tag_id int
        
          indexes  {
            post_id
          }
        """.trimIndent()

        val indexes = IndexParser.parseIndexes(input)

        assertEquals(1, indexes.size)
        assertEquals("post_id", indexes[0].name)
    }

    @Test
    fun `index with no expression`() {
        val input = """
          id int [pk]
          post_id int
          tag_id int
        
          indexes  {
            (`id*3`,`getdate()`)
          }
        """.trimIndent()

        val indexes = IndexParser.parseIndexes(input)

        assertEquals(1, indexes.size)
        assertEquals(listOf("`id*3`", "`getdate()`"), indexes[0].columnNames)
    }

    @Test
    fun `parses name of index`() {
        val input = """
          id int [pk]
          post_id int
          tag_id int
        
          Indexes  {
            (post_id) [name:'index_post_tags_post_id', unique]
            (tag_id) [name:'index_post_tags_tag_id']
          }
        """.trimIndent()

        val indexes = IndexParser.parseIndexes(input)

        assertEquals("index_post_tags_post_id", indexes[0].name)
        assertEquals("index_post_tags_tag_id", indexes[1].name)
    }

    @Test
    fun `parses uniqueness of index`() {
        val input = """
          id int [pk]
          post_id int
          tag_id int
        
          Indexes  {
            (post_id) [name:'index_post_tags_post_id', unique]
            (tag_id) [name:'index_post_tags_tag_id']
          }
        """.trimIndent()

        val indexes = IndexParser.parseIndexes(input)

        assertEquals(true, indexes[0].unique)
        assertEquals(false, indexes[1].unique)
    }

    @Test
    fun `parses multiple of indexes`() {
        val input = """
          id int [pk]
          post_id int
          tag_id int
        
          Indexes  {
            (post_id) [name:'index_post_tags_post_id', unique]
            (tag_id) [name:'index_post_tags_tag_id']
          }
        """.trimIndent()

        val indexes = IndexParser.parseIndexes(input)

        assertEquals(2, indexes.size)
    }

    @Test
    fun `parses column names of indexes`() {
        val input = """
          id int [pk]
          post_id int
          tag_id int
        
          Indexes  {
            (post_id) [name:'index_post_tags_post_id', unique]
            (tag_id, post_id) [name:'index_post_tags_tag_id']
          }
        """.trimIndent()

        val indexes = IndexParser.parseIndexes(input)

        assertEquals(listOf("post_id"), indexes[0].columnNames)
        assertEquals(listOf("tag_id", "post_id"), indexes[1].columnNames)
    }
}