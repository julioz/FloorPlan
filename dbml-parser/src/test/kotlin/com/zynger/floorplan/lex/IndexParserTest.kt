package com.zynger.floorplan.lex

import org.junit.Assert.*
import org.junit.Ignore
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

    @Ignore(value = "Unsupported: advanced usage of index.")
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
    fun `unique index with no explicit name`() {
        val input = """
          id int [pk]
          post_id int
          tag_id int
        
          indexes  {
            (id, post_id) [unique]
          }
        """.trimIndent()

        val indexes = IndexParser.parseIndexes(input)

        assertEquals(1, indexes.size)
        assertEquals("unnamed_index", indexes[0].name)
        assertEquals(true, indexes[0].unique)
    }

    @Test
    fun `composite primary key in indexes block gets removed`() {
        val input = """
          "user_id" int(11) [not null, default: "0"]
          "track_id" int(11) [not null, default: "0"]
          "created_at" datetime(6) [default: NULL]

          Indexes {
            (user_id, created_at) [name: "index_user_id_created_at"]
            (track_id, created_at) [name: "index_track_id_created_at"]
            (user_id, track_id) [pk]
          }
        """.trimIndent()

        val indexes = IndexParser.parseIndexes(input)

        assertEquals(2, indexes.size)
        assertEquals("index_user_id_created_at", indexes[0].name)
        assertEquals("index_track_id_created_at", indexes[1].name)
    }

    @Ignore(value = "Unsupported: advanced usage of index.")
    @Test
    fun `index as expression`() {
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

    @Ignore(value = "Unsupported: advanced usage of index.")
    @Test
    fun `generates name of index when not explicitly specified`() {
        val input = """
          id int [pk]
          post_id int
          tag_id int
        
          Indexes  {
            (post_id)
            (tag_id, post_id)
          }
        """.trimIndent()

        val indexes = IndexParser.parseIndexes(input)

        assertEquals("unnamed_index", indexes[0].name)
        assertEquals("unnamed_index", indexes[1].name)
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

    @Test
    fun `parses composite primary key`() {
        val input = """
          "user_id" int(11) [not null, default: "0"]
          "track_id" int(11) [not null, default: "0"]
          "created_at" datetime(6) [default: NULL]

          Indexes {
            (user_id, created_at) [name: "index_user_id_created_at"]
            (track_id, created_at) [name: "index_track_id_created_at"]
            (user_id, track_id) [pk]
          }
        """.trimIndent()

        val compositePrimaryKeys = IndexParser.parseCompositePrimaryKeys(input)

        assertEquals(1, compositePrimaryKeys.size)
        assertEquals(2, compositePrimaryKeys[0].columnNames.size)
        assertEquals("user_id", compositePrimaryKeys[0].columnNames[0])
        assertEquals("track_id", compositePrimaryKeys[0].columnNames[1])
    }
}