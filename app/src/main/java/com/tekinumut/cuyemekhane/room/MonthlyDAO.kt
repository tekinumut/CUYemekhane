package com.tekinumut.cuyemekhane.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tekinumut.cuyemekhane.models.*

@Dao
interface MonthlyDAO {

    @Query("Select * from FoodDate")
    fun getDaysOfMonth(): LiveData<List<FoodDate>>

    @Transaction
    @Query("Select *from FoodDate where selectedDay = 1 LIMIT 1")
    fun getSelectedDayOfMonth(): LiveData<DateWithFoodDetailComp>

    @Query("Delete from FoodDate where selectedDay = 1")
    suspend fun removeSelectedDayOfMonth()

    @Query("Update FoodDate set selectedDay = 0")
    suspend fun resetSelectedDayOfMonth()

    @Query("Update FoodDate set selectedDay = 1 where name LIKE '%' || :datePart || '%' or name = :datePart")
    suspend fun setSelectedDayOfMonth(datePart: String)

    @Transaction
    suspend fun updateSelectedDayOfMonth(datePart: String) {
        resetSelectedDayOfMonth()
        setSelectedDayOfMonth(datePart)
    }

    // İki Veritabanının Ortak Kısımlar
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
    suspend fun addAllValues(listOfAll: ListOfAll) {
        removeAllData()
        addDate(listOfAll.foodDate)
        addFood(listOfAll.food)
        addFoodDetail(listOfAll.foodDetail)
        addComponent(listOfAll.foodComponent)
    }

    /**
     * Foreign key tanımlandığından tüm veriler silinecek.
     */
    @Query("Delete from FoodDate")
    suspend fun removeAllData()
}