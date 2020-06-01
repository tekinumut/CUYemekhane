package com.tekinumut.cuyemekhane.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.models.*

@Database(entities = [FoodDate::class, Food::class, FoodDetail::class, FoodComponent::class, Duyurular::class], version = 1)
abstract class DailyDatabase : RoomDatabase() {

    abstract fun yemekDao(): DailyDAO

    companion object {
        @Volatile
        private var INSTANCE: DailyDatabase? = null

        fun getInstance(context: Context): DailyDatabase {
            val tempInstance = INSTANCE
            tempInstance?.let { return tempInstance }

            synchronized(DailyDatabase::class) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DailyDatabase::class.java,
                    ConstantsGeneral.dbNameDaily
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}