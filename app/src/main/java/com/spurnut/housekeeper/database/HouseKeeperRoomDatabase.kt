package com.spurnut.housekeeper.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spurnut.housekeeper.converter.DateConverter
import com.spurnut.housekeeper.converter.UrgencyImportantQuadrantConverter
import com.spurnut.housekeeper.database.dao.HouseDao
import com.spurnut.housekeeper.database.dao.TaskDao
import com.spurnut.housekeeper.database.dao.TaskPhotoDao
import com.spurnut.housekeeper.database.dao.UserDao
import com.spurnut.housekeeper.database.enity.House
import com.spurnut.housekeeper.database.enity.Task
import com.spurnut.housekeeper.database.enity.TaskPhoto
import com.spurnut.housekeeper.database.enity.User
import kotlinx.coroutines.CoroutineScope

@Database(entities = arrayOf(House::class, Task::class, TaskPhoto::class, User::class), version = 1)
@TypeConverters(DateConverter::class, UrgencyImportantQuadrantConverter::class)
abstract class HouseKeeperRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao
    abstract fun houseDao(): HouseDao
    abstract fun taskPhotoDao(): TaskPhotoDao

    companion object {
        @Volatile
        private var INSTANCE: HouseKeeperRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): HouseKeeperRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        HouseKeeperRoomDatabase::class.java,
                        "House_Keeper_database"
                )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this codelab.
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}