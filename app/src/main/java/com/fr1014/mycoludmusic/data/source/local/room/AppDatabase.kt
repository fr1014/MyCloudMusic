package com.fr1014.mycoludmusic.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fr1014.mycoludmusic.data.entity.room.MusicEntity

@Database(entities = arrayOf(MusicEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
}