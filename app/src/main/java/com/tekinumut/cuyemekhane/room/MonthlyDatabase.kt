package com.tekinumut.cuyemekhane.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import com.tekinumut.cuyemekhane.models.Food
import com.tekinumut.cuyemekhane.models.FoodComponent
import com.tekinumut.cuyemekhane.models.FoodDate
import com.tekinumut.cuyemekhane.models.FoodDetail

@Database(entities = [FoodDate::class, Food::class, FoodDetail::class, FoodComponent::class], version = 1)
abstract class MonthlyDatabase : RoomDatabase() {

    abstract fun yemekDao(): FoodDAO

    companion object {
        @Volatile
        private var INSTANCE: MonthlyDatabase? = null

        fun getInstance(context: Context): MonthlyDatabase {
            val tempInstance = INSTANCE
            tempInstance?.let { return tempInstance }

            synchronized(MonthlyDatabase::class) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MonthlyDatabase::class.java,
                    ConstantsGeneral.dbNameMonthly
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}