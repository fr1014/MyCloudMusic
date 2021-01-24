package com.fr1014.mycoludmusic.data.source.local.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.data.entity.room.MusicEntity


@Database(entities = [MusicEntity::class, MusicLike::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao
    abstract fun musicLikeDao(): MusicLikeDao

    companion object {
        val instance = Single.sin
    }

    private object Single {

        val sin: AppDatabase = Room.databaseBuilder(
                MyApplication.getInstance(),
                AppDatabase::class.java,
                "Sample.db"
        )
//                .addMigrations(MIGRATION_1_2)
                .build()
    }
}