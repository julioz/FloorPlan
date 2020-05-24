package com.zynger.floorplan.room

import com.zynger.floorplan.dbml.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File
import com.zynger.floorplan.room.Index as RoomIndex

object RoomConsumer {
    private val json = Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true, isLenient = true))

    fun read(src: File): Project {
        val roomDatabase = json
            .parse(Schema.serializer(), src.readText())
            .database

        val tables: List<Table> = roomDatabase.entities.map {
            Table(
                rawValue = it.createSql, // TODO double-check usages
                name = it.tableName,
                columns = it.fields.map { field ->
                    val primaryKey = it.primaryKey
                    field.toDbmlColumn(primaryKey.columnNames.contains(field.columnName), primaryKey.autoGenerate)
                },
                indexes = it.indices.map { index -> index.toDbmlIndex() }
            )
        }
        val references: List<Reference> = roomDatabase.entities.map { fromEntity ->
            val fromTable = fromEntity.tableName
            fromEntity.foreignKeys.map { foreignKey ->
                val toTable = foreignKey.table
                val edges: List<Pair<String, String>> = foreignKey.edges()
                edges.map { (fromColumn, toColumn) ->
                    Reference(
                        fromTable = fromTable,
                        fromColumn = fromColumn,
                        toTable = toTable,
                        toColumn = toColumn,
                        referenceOrder = null // Room does not provide the order in its schema
                    )
                }
            }.flatten()
        }.flatten()

        return Project(tables, references)
    }

    private fun Field.toDbmlColumn(primaryKey: Boolean, autoGenerate: Boolean): Column {
        return Column(
            rawValue = fieldPath,
            name = columnName,
            type = affinity,
            notNull = notNull,
            primaryKey = primaryKey,
            increment = autoGenerate
        )
    }

    private fun RoomIndex.toDbmlIndex(): Index {
        return Index(
            name = name,
            columnNames = columnNames,
            unique = unique
        )
    }

    private fun ForeignKey.edges(): List<Pair<String, String>> {
        return columns.map { fromColumn ->
            referencedColumns.map { toColumn -> fromColumn to toColumn }
        }.flatten()
    }
}
