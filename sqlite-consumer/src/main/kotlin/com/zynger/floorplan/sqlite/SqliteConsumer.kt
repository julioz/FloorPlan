package com.zynger.floorplan.sqlite

import com.zynger.floorplan.dbml.Column
import com.zynger.floorplan.dbml.Index
import com.zynger.floorplan.dbml.Project
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

object SqliteConsumer {
    fun read(src: File): Project {
        DriverManager.getConnection("jdbc:sqlite:${src.path}").use {
            val tables = it.getTables()

            tables.forEach { table ->
                val tableName = table.name
                val columns = it.getColumns(tableName)
                val indexes = it.getIndexes(tableName)
                TODO("Hey $indexes")

            }

            TODO("Hey $tables")
        }
    }

    private fun Connection.getTables(): List<SQLiteTable> {
        val tablesResultSet: ResultSet = createStatement()
            .executeQuery(
                """
            SELECT name, sql FROM sqlite_master
            WHERE type='table'
            ORDER BY name;
            """.trimIndent()
            )

        val tables = mutableListOf<SQLiteTable>()
        while (tablesResultSet.next()) {
            tables += SQLiteTable(
                name = tablesResultSet.getString("name"),
                sql = tablesResultSet.getString("sql")
            )
        }
        return tables
    }

    private fun Connection.getColumns(tableName: String): List<Column> {
        val columns = mutableListOf<Column>()
        val columnsResultSet: ResultSet = createStatement().executeQuery("PRAGMA table_info($tableName);")
        while (columnsResultSet.next()) {
            columns += Column(
                name = columnsResultSet.getString("name"),
                type = columnsResultSet.getString("type"),
                defaultValue = columnsResultSet.getString("Dflt_value"),
                primaryKey = columnsResultSet.getBoolean("Pk"),
                notNull = columnsResultSet.getBoolean("Notnull"),
                increment = false // FIXME check indexes (https://stopbyte.com/t/how-to-check-if-a-column-is-autoincrement-primary-key-or-not-in-sqlite/174/2 or https://stackoverflow.com/questions/20979239/how-to-tell-if-a-sqlite-column-is-autoincrement)
            )
        }
        return columns
    }

    private fun Connection.getIndexes(tableName: String): List<Index> {
        val indexes = mutableListOf<Index>()
        val indexesResultSet: ResultSet = createStatement().executeQuery("PRAGMA index_list($tableName);")
        while (indexesResultSet.next()) {
            val indexName: String = indexesResultSet.getString("name")
            val unique: Boolean = indexesResultSet.getBoolean("unique")

            indexes += Index(
                name = indexName,
                columnNames = getIndexTargetColumns(indexName),
                unique = unique
            )
        }
        return indexes
    }

    private fun Connection.getIndexTargetColumns(indexName: String): List<String> {
        val indexInfos: ResultSet = createStatement().executeQuery("PRAGMA index_xinfo($indexName);")

        val columnTargets = mutableListOf<String>()
        while (indexInfos.next()) {
            val columnName: String? = indexInfos.getString("name")
            if (columnName != null) {
                columnTargets += columnName
            }
        }
        return columnTargets
    }

    private data class SQLiteTable(val name: String, val sql: String)
}