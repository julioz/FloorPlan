package com.zynger.floorplan

import com.zynger.floorplan.dbml.Project
import java.io.File

interface Consumer {
    fun read(src: File): Project
}
