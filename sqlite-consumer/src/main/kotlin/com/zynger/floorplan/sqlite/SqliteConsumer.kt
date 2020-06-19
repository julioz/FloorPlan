package com.zynger.floorplan.sqlite

import com.zynger.floorplan.dbml.*
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

object SqliteConsumer {

    fun read(src: File): Project {
        return DriverManager.getConnection("jdbc:sqlite:${src.path}").use {
            val allTables = mutableListOf<Table>()
            val allReferences = mutableListOf<Reference>()

            it.getTables()
                .filterNot { table -> table.isSystemTable() }
                .forEach { table ->
                    val tableName = table.name

                    allTables += Table(
                        rawValue = table.sql,
                        name = tableName,
                        columns = it.getColumns(tableName),
                        indexes = it.getIndexes(tableName)
                    )
                    allReferences += it.getReferences(tableName)
                }

            Project(
                tables = allTables,
                references = allReferences
            )
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
            val primaryKey = columnsResultSet.getBoolean("Pk")
            columns += Column(
                name = columnsResultSet.getString("name"),
                type = columnsResultSet.getString("type"),
                defaultValue = columnsResultSet.getString("Dflt_value"),
                primaryKey = primaryKey,
                notNull = columnsResultSet.getBoolean("Notnull"),
                increment = if (primaryKey) isAutoIncrement(tableName) else false
            )
        }
        return columns
    }

    private fun Connection.isAutoIncrement(tableName: String): Boolean {
        val autoIncrementResultSet =
            createStatement().executeQuery("SELECT * FROM sqlite_master WHERE tbl_name=\"$tableName\" AND sql LIKE \"%AUTOINCREMENT%\"")
        return autoIncrementResultSet.next()
    }

    private fun Connection.getReferences(tableName: String): List<Reference> {
        val references = mutableListOf<Reference>()
        val foreignKeys = createStatement().executeQuery("PRAGMA foreign_key_list($tableName);")

        while (foreignKeys.next()) {
            references += Reference(
                fromTable = tableName,
                toTable = foreignKeys.getString("table"),
                fromColumn = foreignKeys.getString("from"),
                toColumn = foreignKeys.getString("to"),
                referenceOrder = ReferenceOrder.OneToOne, // FIXME: how to precise the reference order?
                updateAction = foreignKeys.getString("on_update"),
                deleteAction = foreignKeys.getString("on_delete")
            )
        }
        return references
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

    private fun SQLiteTable.isSystemTable(): Boolean {
        // https://www.techonthenet.com/sqlite/sys_tables/index.php
        return name.startsWith("sqlite_")
    }
}
