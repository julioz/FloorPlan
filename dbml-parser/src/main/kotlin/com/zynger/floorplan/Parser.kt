package com.zynger.floorplan

class Parser {

    fun parse(dbmlInput: String) {
        val tableMatches = TABLE_REGEX.findAll(dbmlInput).toList()

        val references = REFERENCE_REGEX.findAll(dbmlInput).map {
            Reference(
                fromTable = it.groups[1]!!.value,
                fromColumn = it.groups[2]!!.value,
                toTable = it.groups[4]!!.value,
                toColumn = it.groups[5]!!.value,
                referenceOrder = ReferenceOrder.fromString(it.groups[3]!!.value)
            )
        }.toList()
    }

    companion object {
        private val TABLE_REGEX = Regex("""[Tt]able\s+(."\w+"|\w+.)\s*(\s*as\s+[\w]+|\s*as\s+"[\w]+"|)(\s\[.*]|)\s*\{(\s|\n|[^}]*)}""")
        private val REFERENCE_REGEX = Regex("""[Rr]ef:\s*("\w+"|\w+)\.("\w+"|\w+)\s+([-<>])\s+("\w+"|\w+)\.("\w+"|\w+)""")
    }
}
