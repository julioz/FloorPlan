package com.zynger.floorplan.lex

import com.zynger.floorplan.dbml.Reference
import com.zynger.floorplan.dbml.ReferenceOrder
import org.intellij.lang.annotations.Language

object LoneReferenceParser {
    @Language("RegExp") private const val WORD = """("\w+"|\w+)+"""
    @Language("RegExp") private const val REFERENCE_ORDER = """([-<>])"""
    private val REFERENCE_REGEX = Regex("""[Rr]ef:\s*$WORD\.$WORD\s+$REFERENCE_ORDER\s+$WORD\.$WORD""")

    fun parseReferences(dbmlInput: String): List<Reference> {
        return REFERENCE_REGEX.findAll(dbmlInput).map {
            Reference(
                rawValue = it.groups[0]!!.value,
                fromTable = it.groups[1]!!.value.removeSurroundQuotes(),
                fromColumn = it.groups[2]!!.value.removeSurroundQuotes(),
                referenceOrder = ReferenceOrder.fromString(it.groups[3]!!.value),
                toTable = it.groups[4]!!.value.removeSurroundQuotes(),
                toColumn = it.groups[5]!!.value.removeSurroundQuotes()
            )
        }.toList()
    }

    private fun String.removeSurroundQuotes(): String {
        return this.removeSurrounding("\"")
    }
}
