package com.tekinumut.cuyemekhane.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tekinumut.cuyemekhane.models.*

@Dao
interface FoodDAO {

    @Transaction
    @Query("Select *from FoodDate")
    fun getMonthlyList(): LiveData<List<DateWithFoodDetailComp>>

    @Transaction
    @Query("Select *from FoodDate where name = :name")
    fun getSelectedDay(name: String): LiveData<DateWithFoodDetailComp>

    @Query("Delete from FoodDate where name = :name")
    fun removeDayOfMonth(name: String)

    @Transaction
    @Query("Select *from FoodDate LIMIT 1")
    fun getDailyList(): LiveData<DateWithFoodDetailComp>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDate(dates: List<FoodDate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFood(foods: List<Food>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFoodDetail(foodDetails: List<FoodDetail>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addComponent(foodComponents: List<FoodComponent>)

    /**
     * Tüm veritabanı temizlendikten sonra
     * Tarih, yemek, yemek içeriği gibi veriler tekrar veritabanına yazılır
     */
    @Transaction
    suspend fun addAllValues(
        dates: List<FoodDate>,
        foods: List<Food>,
        foodDetails: List<FoodDetail>,
        foodComponent: List<FoodComponent>
    ) {
        removeAllData()
        addDate(dates)
        addFood(foods)
        addFoodDetail(foodDetails)
        addComponent(foodComponent)
    }

    /**
     * Foreign key tanımlandığından tüm veriler silinecek.
     */
    @Query("Delete from FoodDate")
    suspend fun removeAllData()
}