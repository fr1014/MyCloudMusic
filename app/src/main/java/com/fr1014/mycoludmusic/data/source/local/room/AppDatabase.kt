package com.fr1014.mycoludmusic.data.source.local.room

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fr1014.mycoludmusic.app.MyApplication
import com.fr1014.mycoludmusic.data.entity.room.MusicEntity


@Database(entities = arrayOf(MusicEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicDao(): MusicDao

//    @VisibleForTesting
//    object MIGRATION_1_2: Migration(1,2) {
//        override fun migrate(database: SupportSQLiteDatabase) {
//            database.execSQL("ALTER TABLE MusicEntity ADD COLUMN history INTEGER NOT NULL DEFAULT 1")
//        }
//    }

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