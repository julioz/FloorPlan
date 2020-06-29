package com.zynger.floorplan.gradle.sample

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1
)
abstract class MyDatabase : RoomDatabase()
