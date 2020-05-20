package com.zynger.floorplan.dbml

import com.zynger.floorplan.Settings
import com.zynger.floorplan.room.*
import org.junit.Assert.assertEquals
import org.junit.Test

class TableTest {
    companion object {
        private const val TABLE_NAME = "Users"
        private const val TABLE_NAME_2 = "Songs"
        private const val TABLE_NAME_3 = "Albums"
        private const val COLUMN_NAME_1 = "userId"
        private val FIELD_1 = Field(COLUMN_NAME_1, COLUMN_NAME_1, "TEXT", true)
        private const val COLUMN_NAME_2 = "age"
        private val FIELD_2 = Field(COLUMN_NAME_2, COLUMN_NAME_2, "INTEGER", false)
        private const val COLUMN_NAME_3 = "username"
        private val FIELD_3 = Field(COLUMN_NAME_3, COLUMN_NAME_3, "TEXT", true)
        private const val CREATE_SQL = "CREATE TABLE MyTable"
        private const val INDEX_1_NAME = "index1"
        private const val INDEX_2_NAME = "index2"

        private val ON_DELETE = ForeignKeyAction.NO_ACTION
        private val ON_UPDATE = ForeignKeyAction.NO_ACTION

        private val DEFAULT_SETTINGS = Settings()
    }

    @Test
    fun `table with single field`() {
        val table = Table(
            Entity(
                tableName = TABLE_NAME,
                createSql = CREATE_SQL,
                fields = listOf(FIELD_1),
                primaryKey = PrimaryKey(emptyList(), false),
                indices = emptyList(),
                foreignKeys = emptyList()
            ),
            DEFAULT_SETTINGS
        )

        assertEquals(
            """
                Table $TABLE_NAME {
                  $COLUMN_NAME_1 varchar [note: 'not null']
                }
            """.trimIndent(),
            table.toString()
        )
    }

    @Test
    fun `table with single field and creation SQL as note`() {
        val table = Table(
            Entity(
                tableName = TABLE_NAME,
                createSql = CREATE_SQL,
                fields = listOf(FIELD_1),
                primaryKey = PrimaryKey(emptyList(), false),
                indices = emptyList(),
                foreignKeys = emptyList()
            ),
            Settings(creationSqlAsTableNote = true)
        )

        assertEquals(
            """
                Table $TABLE_NAME {
                  $COLUMN_NAME_1 varchar [note: 'not null']
                  
                  Note: '$CREATE_SQL'
                }
            """.trimIndent(),
            table.toString()
        )
    }

    @Test
    fun `table with multiple fields`() {
        val table = Table(
            Entity(
                tableName = TABLE_NAME,
                createSql = CREATE_SQL,
                fields = listOf(FIELD_1, FIELD_2, FIELD_3),
                primaryKey = PrimaryKey(emptyList(), false),
                indices = emptyList(),
                foreignKeys = emptyList()
            ),
            DEFAULT_SETTINGS
        )

        assertEquals(
            """
                Table $TABLE_NAME {
                  $COLUMN_NAME_1 varchar [note: 'not null']
                  $COLUMN_NAME_2 int [note: 'nullable']
                  $COLUMN_NAME_3 varchar [note: 'not null']
                }
            """.trimIndent(),
            table.toString()
        )
    }

    @Test
    fun `table with single index`() {
        val table = Table(
            Entity(
                tableName = TABLE_NAME,
                createSql = CREATE_SQL,
                fields = listOf(FIELD_1),
                primaryKey = PrimaryKey(emptyList(), false),
                indices = listOf(
                    Index(INDEX_1_NAME, true, listOf(COLUMN_NAME_1), CREATE_SQL)
                ),
                foreignKeys = emptyList()
            ),
            DEFAULT_SETTINGS
        )

        assertEquals(
            """
                Table $TABLE_NAME {
                  $COLUMN_NAME_1 varchar [note: 'not null']
                
                  Indexes  {
                    ($COLUMN_NAME_1) [name:'$INDEX_1_NAME', unique]
                  }
                }
            """.trimIndent(),
            table.toString()
        )
    }

    @Test
    fun `table with multiple indices`() {
        val table = Table(
            Entity(
                tableName = TABLE_NAME,
                createSql = CREATE_SQL,
                fields = listOf(FIELD_1),
                primaryKey = PrimaryKey(emptyList(), false),
                indices = listOf(
                    Index(INDEX_1_NAME, true, listOf(COLUMN_NAME_1), CREATE_SQL),
                    Index(INDEX_2_NAME, false, listOf(COLUMN_NAME_2, COLUMN_NAME_3), CREATE_SQL)
                ),
                foreignKeys = emptyList()
            ),
            DEFAULT_SETTINGS
        )

        assertEquals(
            """
                Table $TABLE_NAME {
                  $COLUMN_NAME_1 varchar [note: 'not null']
                
                  Indexes  {
                    ($COLUMN_NAME_1) [name:'$INDEX_1_NAME', unique]
                    ($COLUMN_NAME_2,$COLUMN_NAME_3) [name:'$INDEX_2_NAME']
                  }
                }
            """.trimIndent(),
            table.toString()
        )
    }

    @Test
    fun `table with multiple indices and creation sql as note`() {
        val table = Table(
            Entity(
                tableName = TABLE_NAME,
                createSql = CREATE_SQL,
                fields = listOf(FIELD_1),
                primaryKey = PrimaryKey(emptyList(), false),
                indices = listOf(
                    Index(INDEX_1_NAME, true, listOf(COLUMN_NAME_1), CREATE_SQL),
                    Index(INDEX_2_NAME, false, listOf(COLUMN_NAME_2, COLUMN_NAME_3), CREATE_SQL)
                ),
                foreignKeys = emptyList()
            ),
            Settings(creationSqlAsTableNote = true)
        )

        assertEquals(
            """
                Table $TABLE_NAME {
                  $COLUMN_NAME_1 varchar [note: 'not null']
                
                  Indexes  {
                    ($COLUMN_NAME_1) [name:'$INDEX_1_NAME', unique]
                    ($COLUMN_NAME_2,$COLUMN_NAME_3) [name:'$INDEX_2_NAME']
                  }
                  
                  Note: '$CREATE_SQL'
                }
            """.trimIndent(),
            table.toString()
        )
    }

    @Test
    fun `table with single reference`() {
        val table = Table(
            Entity(
                tableName = TABLE_NAME,
                createSql = CREATE_SQL,
                fields = listOf(FIELD_1),
                primaryKey = PrimaryKey(emptyList(), false),
                indices = emptyList(),
                foreignKeys = listOf(
                    ForeignKey(TABLE_NAME_2, listOf(COLUMN_NAME_1), listOf(COLUMN_NAME_3), ON_DELETE, ON_UPDATE)
                )
            ),
            DEFAULT_SETTINGS
        )

        assertEquals(
            """
                Table $TABLE_NAME {
                  $COLUMN_NAME_1 varchar [note: 'not null']
                }
                
                Ref: $TABLE_NAME.$COLUMN_NAME_1 - $TABLE_NAME_2.$COLUMN_NAME_3 [delete: no action, update: no action]
            """.trimIndent(),
            table.toString()
        )
    }

    @Test
    fun `table with multiple references`() {
        val table = Table(
            Entity(
                tableName = TABLE_NAME,
                createSql = CREATE_SQL,
                fields = listOf(FIELD_1),
                primaryKey = PrimaryKey(emptyList(), false),
                indices = emptyList(),
                foreignKeys = listOf(
                    ForeignKey(TABLE_NAME_2, listOf(COLUMN_NAME_3), listOf(COLUMN_NAME_1), ON_DELETE, ON_UPDATE),
                    ForeignKey(TABLE_NAME_3, listOf(COLUMN_NAME_1), listOf(COLUMN_NAME_2), ON_DELETE, ON_UPDATE)
                )
            ),
            DEFAULT_SETTINGS
        )

        assertEquals(
            """
                Table Users {
                  userId varchar [note: 'not null']
                }
                
                Ref: Users.username - Songs.userId [delete: no action, update: no action]
                Ref: Users.userId - Albums.age [delete: no action, update: no action]
            """.trimIndent(),
            table.toString()
        )
    }

    @Test
    fun `table with multiple fields, indices and references`() {
        val table = Table(
            Entity(
                tableName = TABLE_NAME,
                createSql = CREATE_SQL,
                fields = listOf(FIELD_1, FIELD_2, FIELD_3),
                primaryKey = PrimaryKey(emptyList(), false),
                indices = listOf(
                    Index(INDEX_1_NAME, true, listOf(COLUMN_NAME_1), CREATE_SQL),
                    Index(INDEX_2_NAME, false, listOf(COLUMN_NAME_2, COLUMN_NAME_3), CREATE_SQL)
                ),
                foreignKeys = listOf(
                    ForeignKey(TABLE_NAME_2, listOf(COLUMN_NAME_3), listOf(COLUMN_NAME_1), ON_DELETE, ON_UPDATE),
                    ForeignKey(TABLE_NAME_3, listOf(COLUMN_NAME_1), listOf(COLUMN_NAME_2), ON_DELETE, ON_UPDATE)
                )
            ),
            DEFAULT_SETTINGS
        )

        assertEquals(
            """
                Table Users {
                  userId varchar [note: 'not null']
                  age int [note: 'nullable']
                  username varchar [note: 'not null']
                
                  Indexes  {
                    (userId) [name:'index1', unique]
                    (age,username) [name:'index2']
                  }
                }
                
                Ref: Users.username - Songs.userId [delete: no action, update: no action]
                Ref: Users.userId - Albums.age [delete: no action, update: no action]
            """.trimIndent(),
            table.toString()
        )
    }

    @Test
    fun `table with multiple fields, indices, references and creation sql as note`() {
        val table = Table(
            Entity(
                tableName = TABLE_NAME,
                createSql = CREATE_SQL,
                fields = listOf(FIELD_1, FIELD_2, FIELD_3),
                primaryKey = PrimaryKey(emptyList(), false),
                indices = listOf(
                    Index(INDEX_1_NAME, true, listOf(COLUMN_NAME_1), CREATE_SQL),
                    Index(INDEX_2_NAME, false, listOf(COLUMN_NAME_2, COLUMN_NAME_3), CREATE_SQL)
                ),
                foreignKeys = listOf(
                    ForeignKey(TABLE_NAME_2, listOf(COLUMN_NAME_3), listOf(COLUMN_NAME_1), ON_DELETE, ON_UPDATE),
                    ForeignKey(TABLE_NAME_3, listOf(COLUMN_NAME_1), listOf(COLUMN_NAME_2), ON_DELETE, ON_UPDATE)
                )
            ),
            Settings(creationSqlAsTableNote = true)
        )

        assertEquals(
            """
                Table Users {
                  userId varchar [note: 'not null']
                  age int [note: 'nullable']
                  username varchar [note: 'not null']
                
                  Indexes  {
                    (userId) [name:'index1', unique]
                    (age,username) [name:'index2']
                  }
                  
                  Note: '$CREATE_SQL'
                }
                
                Ref: Users.username - Songs.userId [delete: no action, update: no action]
                Ref: Users.userId - Albums.age [delete: no action, update: no action]
            """.trimIndent(),
            table.toString()
        )
    }
}