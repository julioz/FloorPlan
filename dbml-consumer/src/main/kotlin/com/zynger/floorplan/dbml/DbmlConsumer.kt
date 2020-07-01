package com.zynger.floorplan.dbml

import com.zynger.floorplan.Consumer
import com.zynger.floorplan.Parser
import java.io.File

object DbmlConsumer: Consumer {
    override fun read(src: File): Project {
        return Parser.parse(src.readText())
    }
}
