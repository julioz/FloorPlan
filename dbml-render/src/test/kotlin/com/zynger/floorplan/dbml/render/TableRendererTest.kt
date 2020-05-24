package com.zynger.floorplan.dbml.render

import com.zynger.floorplan.Settings
import com.zynger.floorplan.dbml.*
import org.junit.Assert.assertEquals
import org.junit.Test

class TableRendererTest {
    companion object {
        private const val TABLE_NAME = "Users"
        private const val TABLE_NAME_2 = "Songs"
        private const val TABLE_NAME_3 = "Albums"
        private const val COLUMN_NAME_1 = "userId"
        private val FIELD_1 = Column(
            rawValue = COLUMN_NAME_1,
            name = COLUMN_NAME_1, type = "TEXT", notNull = true)
        private const val COLUMN_NAME_2 = "age"
        private val FIELD_2 = Column(
            rawValue = COLUMN_NAME_2,
            name = COLUMN_NAME_2, type = "INTEGER", notNull = false)
        private const val COLUMN_NAME_3 = "username"
        private val FIELD_3 = Column(
            rawValue = COLUMN_NAME_3,
            name = COLUMN_NAME_3, type = "TEXT", notNull = true)
        private const val CREATE_SQL = "CREATE TABLE MyTable"
        private const val INDEX_1_NAME = "index1"
        private const val INDEX_2_NAME = "index2"

        private const val ON_DELETE = "no action"
        private const val ON_UPDATE = "no action"

        private val DEFAULT_SETTINGS = Settings()
    }

    @Test
    fun `table with single field`() {
        val table = TableRenderer(
            Table(
                name = TABLE_NAME,
                rawValue = CREATE_SQL,
                columns = listOf(FIELD_1),
                indexes = emptyList()
            ),
            referencesFromTable = emptyList(),
            settings = DEFAULT_SETTINGS
        )

        assertEquals(
            """
                Table $TABLE_NAME {
                  $COLUMN_NAME_1 varchar [note: 'not null']
                }
            """.trimIndent(),
            table.render()
        )
    }

    @Test
    fun `table with single field and creation SQL as note`() {
        val table = TableRenderer(
            Table(
                name = TABLE_NAME,
                rawValue = CREATE_SQL,
                columns = listOf(FIELD_1),
                indexes = emptyList()
            ),
            referencesFromTable = emptyList(),
            settings = Settings(creationSqlAsTableNote = true)
        )

        assertEquals(
            """
                Table $TABLE_NAME {
                  $COLUMN_NAME_1 varchar [note: 'not null']
                  
                  Note: '$CREATE_SQL'
                }
            """.trimIndent(),
            table.render()
        )
    }

    @Test
    fun `table with multiple columns`() {
        val table = TableRenderer(
            Table(
                name = TABLE_NAME,
                rawValue = CREATE_SQL,
                columns = listOf(
                    FIELD_1,
                    FIELD_2,
                    FIELD_3
                ),
                indexes = emptyList()
            ),
            referencesFromTable = emptyList(),
            settings = DEFAULT_SETTINGS
        )

        assertEquals(
            """
                Table $TABLE_NAME {
                  $COLUMN_NAME_1 varchar [note: 'not null']
                  $COLUMN_NAME_2 int [note: 'nullable']
                  $COLUMN_NAME_3 varchar [note: 'not null']
                }
            """.trimIndent(),
            table.render()
        )
    }

    @Test
    fun `table with single index`() {
        val table = TableRenderer(
            Table(
                name = TABLE_NAME,
                rawValue = CREATE_SQL,
                columns = listOf(FIELD_1),
                indexes = listOf(
                    Index(
                        name = INDEX_1_NAME,
                        unique = true,
                        columnNames = listOf(COLUMN_NAME_1)
                    )
                )
            ),
            referencesFromTable = emptyList(),
            settings = DEFAULT_SETTINGS
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
            table.render()
        )
    }

    @Test
    fun `table with multiple indexes`() {
        val table = TableRenderer(
            Table(
                name = TABLE_NAME,
                rawValue = CREATE_SQL,
                columns = listOf(FIELD_1),
                indexes = listOf(
                    Index(
                        name = INDEX_1_NAME,
                        unique = true,
                        columnNames = listOf(COLUMN_NAME_1)
                    ),
                    Index(
                        name = INDEX_2_NAME,
                        unique = false,
                        columnNames = listOf(
                            COLUMN_NAME_2,
                            COLUMN_NAME_3
                        )
                    )
                )
            ),
            referencesFromTable = emptyList(),
            settings = DEFAULT_SETTINGS
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
            table.render()
        )
    }

    @Test
    fun `table with multiple indexes and creation sql as note`() {
        val table = TableRenderer(
            Table(
                name = TABLE_NAME,
                rawValue = CREATE_SQL,
                columns = listOf(FIELD_1),
                indexes = listOf(
                    Index(
                        name = INDEX_1_NAME,
                        unique = true,
                        columnNames = listOf(COLUMN_NAME_1)
                    ),
                    Index(
                        name = INDEX_2_NAME,
                        unique = false,
                        columnNames = listOf(
                            COLUMN_NAME_2,
                            COLUMN_NAME_3
                        )
                    )
                )
            ),
            referencesFromTable = emptyList(),
            settings = Settings(creationSqlAsTableNote = true)
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
            table.render()
        )
    }

    @Test
    fun `table with single reference`() {
        val table = TableRenderer(
            Table(
                name = TABLE_NAME,
                rawValue = CREATE_SQL,
                columns = listOf(FIELD_1),
                indexes = emptyList()
            ),
            referencesFromTable = listOf(
                Reference(
                    fromTable = TABLE_NAME,
                    toTable = TABLE_NAME_2,
                    fromColumn = COLUMN_NAME_1,
                    toColumn = COLUMN_NAME_3,
                    referenceOrder = ReferenceOrder.OneToOne,
                    deleteAction = ON_DELETE,
                    updateAction = ON_UPDATE
                )
            ),
            settings = DEFAULT_SETTINGS
        )

        assertEquals(
            """
                Table $TABLE_NAME {
                  $COLUMN_NAME_1 varchar [note: 'not null']
                }
                
                Ref: $TABLE_NAME.$COLUMN_NAME_1 - $TABLE_NAME_2.$COLUMN_NAME_3 [delete: no action, update: no action]
            """.trimIndent(),
            table.render()
        )
    }

    @Test
    fun `table with multiple references`() {
        val table = TableRenderer(
            Table(
                name = TABLE_NAME,
                rawValue = CREATE_SQL,
                columns = listOf(FIELD_1),
                indexes = emptyList()
            ),
            referencesFromTable = listOf(
                Reference(
                    fromTable = TABLE_NAME,
                    toTable = TABLE_NAME_2,
                    fromColumn = COLUMN_NAME_3,
                    toColumn = COLUMN_NAME_1,
                    deleteAction = ON_DELETE,
                    updateAction = ON_UPDATE,
                    referenceOrder = ReferenceOrder.OneToOne
                ),
                Reference(
                    fromTable = TABLE_NAME,
                    toTable = TABLE_NAME_3,
                    fromColumn = COLUMN_NAME_1,
                    toColumn = COLUMN_NAME_2,
                    deleteAction = ON_DELETE,
                    updateAction = ON_UPDATE,
                    referenceOrder = ReferenceOrder.OneToOne
                )
            ),
            settings = DEFAULT_SETTINGS
        )

        assertEquals(
            """
                Table Users {
                  userId varchar [note: 'not null']
                }
                
                Ref: Users.username - Songs.userId [delete: no action, update: no action]
                Ref: Users.userId - Albums.age [delete: no action, update: no action]
            """.trimIndent(),
            table.render()
        )
    }

    @Test
    fun `table with multiple columns, indexes and references`() {
        val table = TableRenderer(
            Table(
                name = TABLE_NAME,
                rawValue = CREATE_SQL,
                columns = listOf(
                    FIELD_1,
                    FIELD_2,
                    FIELD_3
                ),
                indexes = listOf(
                    Index(
                        name = INDEX_1_NAME,
                        unique = true,
                        columnNames = listOf(COLUMN_NAME_1)
                    ),
                    Index(
                        name = INDEX_2_NAME,
                        unique = false,
                        columnNames = listOf(
                            COLUMN_NAME_2,
                            COLUMN_NAME_3
                        )
                    )
                )
            ),
            referencesFromTable = listOf(
                Reference(
                    fromTable = TABLE_NAME,
                    toTable = TABLE_NAME_2,
                    fromColumn = COLUMN_NAME_3,
                    toColumn = COLUMN_NAME_1,
                    referenceOrder = ReferenceOrder.OneToOne,
                    deleteAction = ON_DELETE,
                    updateAction = ON_UPDATE
                ),
                Reference(
                    fromTable = TABLE_NAME,
                    toTable = TABLE_NAME_3,
                    fromColumn = COLUMN_NAME_1,
                    toColumn = COLUMN_NAME_2,
                    referenceOrder = ReferenceOrder.OneToOne,
                    deleteAction = ON_DELETE,
                    updateAction = ON_UPDATE
                )
            ),
            settings = DEFAULT_SETTINGS
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
            table.render()
        )
    }

    @Test
    fun `table with multiple columns, indexes, references and creation sql as note`() {
        val table = TableRenderer(
            Table(
                name = TABLE_NAME,
                rawValue = CREATE_SQL,
                columns = listOf(
                    FIELD_1,
                    FIELD_2,
                    FIELD_3
                ),
                indexes = listOf(
                    Index(
                        name = INDEX_1_NAME,
                        unique = true,
                        columnNames = listOf(COLUMN_NAME_1)
                    ),
                    Index(
                        name = INDEX_2_NAME,
                        unique = false,
                        columnNames = listOf(
                            COLUMN_NAME_2,
                            COLUMN_NAME_3
                        )
                    )
                )
            ),
            referencesFromTable = listOf(
                Reference(
                    fromTable = TABLE_NAME,
                    toTable = TABLE_NAME_2,
                    fromColumn = COLUMN_NAME_3,
                    toColumn = COLUMN_NAME_1,
                    referenceOrder = ReferenceOrder.OneToOne,
                    deleteAction = ON_DELETE,
                    updateAction = ON_UPDATE
                ),
                Reference(
                    fromTable = TABLE_NAME,
                    toTable = TABLE_NAME_3,
                    fromColumn = COLUMN_NAME_1,
                    toColumn = COLUMN_NAME_2,
                    referenceOrder = ReferenceOrder.OneToOne,
                    deleteAction = ON_DELETE,
                    updateAction = ON_UPDATE
                )
            ),
            settings = Settings(creationSqlAsTableNote = true)
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
            table.render()
        )
    }
}
