package com.zynger.floorplan

object Parser {

    private val TABLE_REGEX = Regex("""[Tt]able\s+(."\w+"|\w+.)\s*(\s*as\s+[\w]+|\s*as\s+"[\w]+"|)(\s\[.*]|)\s*\{(\s|\n|[^}]*)}""")
    private val REFERENCE_REGEX = Regex("""[Rr]ef:\s*("\w+"|\w+)\.("\w+"|\w+)\s+([-<>])\s+("\w+"|\w+)\.("\w+"|\w+)""")
    private val COLUMN_REGEX = Regex("""("\w+"|\w+)+\s+("\w+"|\w+)(\s+\[[^]]*]|)[ ]*\n""")
    private val COLUMN_REFERENCE_REGEX = Regex("""((ref)\s*:\s*([<>-])\s*("\w+"|\w+)\.("\w+"|\w+))""")

    fun parse(dbmlInput: String): Project {
        val tables = parseTables(dbmlInput)
        val columnReferences = tables.map { it.columns }.flatten().mapNotNull { it.reference }
        val references = parseReferences(dbmlInput) + columnReferences
        return Project(tables, references)
    }

    private fun parseTables(dbmlInput: String): List<Table> {
        // TODO: aliases and table notes also get parsed; should we update the modeling to include them?

        return TABLE_REGEX.findAll(dbmlInput).map {
            val tableName = it.groups[1]!!.value.trim()
            val tableContent = it.groups[4]!!.value.run {
                // TODO BUG, hack: the TABLE_REGEX doesn't take in account the Indexes block properly
                val indexesMatchResult = Regex("""Indexes\s+\{""").find(this)
                if (indexesMatchResult != null) {
                    this.substringBefore(indexesMatchResult.value)
                } else {
                    this
                }
            }
            Table(
                rawValue = it.groups[0]!!.value,
                name = tableName,
                columns = parseColumns(tableName, tableContent),
                indexes = emptyList()
            )
        }.toList()
    }

    private fun parseColumns(tableName: String, columnsInput: String): List<Column> {
        return COLUMN_REGEX.findAll(columnsInput).map {
            val rawValue = it.groups[0]!!.value
            val name = it.groups[1]!!.value
            val type = it.groups[2]!!.value
            val columnProperties = it.groups[3]!!.value.trim()
            val notNull = columnProperties.contains("not null")
            val pk = columnProperties.contains("pk")
            val increment = columnProperties.contains("increment")
            val reference: Reference? = if (COLUMN_REFERENCE_REGEX.containsMatchIn(columnProperties)) {
                val referenceProperties = COLUMN_REFERENCE_REGEX.find(columnProperties)!!
                Reference(
                    rawValue = referenceProperties.groups[0]!!.value,
                    fromTable = tableName,
                    fromColumn = name,
                    referenceOrder = ReferenceOrder.fromString(referenceProperties.groups[3]!!.value),
                    toTable = referenceProperties.groups[4]!!.value,
                    toColumn = referenceProperties.groups[5]!!.value
                )
            } else null

            Column(
                rawValue = rawValue,
                name = name,
                type = type,
                primaryKey = pk,
                notNull = notNull,
                increment = increment,
                reference = reference
            )
        }.toList()
    }

    private fun parseReferences(dbmlInput: String): List<Reference> {
        return REFERENCE_REGEX.findAll(dbmlInput).map {
            Reference(
                rawValue = it.groups[0]!!.value,
                fromTable = it.groups[1]!!.value,
                fromColumn = it.groups[2]!!.value,
                referenceOrder = ReferenceOrder.fromString(it.groups[3]!!.value),
                toTable = it.groups[4]!!.value,
                toColumn = it.groups[5]!!.value
            )
        }.toList()
    }
}
