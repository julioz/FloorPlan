package com.sampledata.floorplan

import androidx.room.Database
import androidx.room.RoomDatabase

annotation AnotherAnnotation

@Database
@AnotherAnnotation
interface MyDatabase : RoomDatabase()
