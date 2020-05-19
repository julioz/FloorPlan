package com.zynger.floorplan

import com.zynger.floorplan.lex.LoneReferenceParser
import com.zynger.floorplan.lex.TableParser

object Parser {

    fun parse(dbmlInput: String): Project {
        val tables = TableParser.parseTables(dbmlInput)
        val columnReferences = tables.map { it.columns }.flatten().mapNotNull { it.reference }
        val references = LoneReferenceParser.parseReferences(dbmlInput) + columnReferences
        return Project(tables, references)
    }

}
