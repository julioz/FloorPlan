package com.zynger.floorplan.lex

import com.zynger.floorplan.Reference
import com.zynger.floorplan.ReferenceOrder

object ReferenceParser {
    private val REFERENCE_REGEX = Regex("""[Rr]ef:\s*("\w+"|\w+)\.("\w+"|\w+)\s+([-<>])\s+("\w+"|\w+)\.("\w+"|\w+)""")

    fun parseReferences(dbmlInput: String): List<Reference> {
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
