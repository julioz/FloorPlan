package com.zynger.floorplan.lex

import com.zynger.floorplan.dbml.Table
import org.junit.Assert.*
import org.junit.Test

class TableParserTest {

    @Test
    fun `no tables`() {
        val input = """
            
        """.trimIndent()

        val tables = TableParser.parseTables(input)

        assertEquals(emptyList<Table>(), tables)
    }

    @Test
    fun `parses name of table`() {
        val input = """
            Table posts {
              id int [pk, increment]
              title varchar [not null]
            }
        """.trimIndent()

        val tables = TableParser.parseTables(input)

        assertEquals("posts", tables[0].name)
    }

    @Test
    fun `parses name of table with quotes`() {
        val input = """
            Table "posts" {
              id int [pk, increment]
              title varchar [not null]
            }
        """.trimIndent()

        val tables = TableParser.parseTables(input)

        assertEquals("posts", tables[0].name)
    }

    @Test
    fun `parses alias of table`() {
        val input = """
            Table posts as P {
              id int [pk, increment]
              title varchar [not null]
            }
        """.trimIndent()

        val tables = TableParser.parseTables(input)

        assertEquals("P", tables[0].alias)
    }

    @Test
    fun `parses alias of table with quotes`() {
        val input = """
            Table "posts" as "P" {
              id int [pk, increment]
              title varchar [not null]
            }
        """.trimIndent()

        val tables = TableParser.parseTables(input)

        assertEquals("P", tables[0].alias)
    }

    @Test
    fun `parses name of multiple tables`() {
        val input = """
            Table posts {
              id int [pk, increment]
              title varchar [not null]
            }
            
            table comments {
              id int [pk,increment]
              comment varchar 
              post_id int [not null,ref: > posts.id]
            }
            table tags as aliasForTagsTable{
              id int [pk, increment, not null]
              title varchar [not null]
            }

        """.trimIndent()

        val tables = TableParser.parseTables(input)

        assertEquals("posts", tables[0].name)
        assertEquals("comments", tables[1].name)
        assertEquals("tags", tables[2].name)
    }

    @Test
    fun `passes column content to be parsed by delegation`() {
        val input = """
            table post_tags [note: 'hey table note']{
              id int [pk]
              post_id int
              tag_id int
            }


        """.trimIndent()

        val tables = TableParser.parseTables(input)

        assertEquals(3, tables.first().columns.size)
    }

    @Test
    fun `passes index content to be parsed by delegation`() {
        val input = """
            table post_tags [note: 'hey table note']{
              id int [pk]
              post_id int
              tag_id int

              Indexes  {
                (post_id) [name:'index_post_tags_post_id', unique]
                (tag_id) [name:'index_post_tags_tag_id']
              }
            }
        """.trimIndent()

        val tables = TableParser.parseTables(input)

        assertEquals(2, tables.first().indexes.size)
    }

    @Test
    fun `passes index content with composite primary keys to be parsed by delegation`() {
        val input = """
            table post_tags [note: 'hey table note']{
              id int [pk]
              post_id int
              tag_id int

              Indexes  {
                (post_id) [name:'index_post_tags_post_id', unique]
                (tag_id) [name:'index_post_tags_tag_id']
                (id, post_id) [pk]
              }
            }
        """.trimIndent()

        val tables = TableParser.parseTables(input)

        assertEquals(2, tables.first().indexes.size)
        assertEquals(true, tables.first().columns[0].primaryKey)
        assertEquals(true, tables.first().columns[1].primaryKey)
    }
}
