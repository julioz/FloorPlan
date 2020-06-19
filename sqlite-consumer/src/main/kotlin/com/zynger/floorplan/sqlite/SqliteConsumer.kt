package com.zynger.floorplan.sqlite

import com.zynger.floorplan.dbml.Project
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

object SqliteConsumer {
    fun read(src: File): Project {
        DriverManager.getConnection("jdbc:sqlite:${src.path}").use {
            val tables = it.getTables()

            TODO("Hey $tables")
        }
    }

    private fun Connection.getTables(): List<Table> {
        val tablesResultSet: ResultSet = createStatement()
            .executeQuery(
                """
            SELECT name, sql FROM sqlite_master
            WHERE type='table'
            ORDER BY name;
            """.trimIndent())

        val tables = mutableListOf<Table>()
        while (tablesResultSet.next()) {
            tables += Table(
                name = tablesResultSet.getString("name"),
                sql = tablesResultSet.getString("sql")
            )
        }
        return tables
    }

    private data class Table(val name: String, val sql: String)
}