package com.tekinumut.cuyemekhane.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tekinumut.cuyemekhane.models.*

@Dao
interface FoodDAO {

    @Query("Select * from FoodDate")
    fun getDaysOfMonth(): LiveData<List<FoodDate>>

//    @Transaction
//    @Query("Select * from FoodDate where name LIKE '%' || :datePart || '%' or name = :datePart LIMIT 1")
//    fun getSearchedDay(datePart: String): LiveData<DateWithFoodDetailComp>

    @Transaction
    @Query("Select *from FoodDate where selectedDay = 1 LIMIT 1")
    fun getSelectedDayOfMonth(): LiveData<DateWithFoodDetailComp>

    @Query("Delete from FoodDate where selectedDay = 1")
    fun removeSelectedDayOfMonth()

    @Transaction
    @Query("Select * from FoodDate LIMIT 1")
    fun getDailyList(): LiveData<DateWithFoodDetailComp>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDate(dates: List<FoodDate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFood(foods: List<Food>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFoodDetail(foodDetails: List<FoodDetail>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addComponent(foodComponents: List<FoodComponent>)

    @Query("Update FoodDate set selectedDay = 0")
    fun resetSelectedDayOfMonth()

    @Query("Update FoodDate set selectedDay = 1 where name LIKE '%' || :datePart || '%' or name = :datePart")
    fun setSelectedDayOfMonth(datePart: String)

    @Transaction
    fun updateSelectedDayOfMonth(datePart: String) {
        resetSelectedDayOfMonth()
        setSelectedDayOfMonth(datePart)
    }

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