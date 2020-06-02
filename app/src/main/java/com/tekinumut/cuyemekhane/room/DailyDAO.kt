package com.tekinumut.cuyemekhane.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tekinumut.cuyemekhane.models.*

@Dao
interface DailyDAO {

    @Transaction
    @Query("Select * from FoodDate LIMIT 1")
    fun getDailyList(): LiveData<DateWithFoodDetailComp>

    // ÜCRETLENDİRME

    @Query("Select html from Pricing")
    fun getPricingHtml(): LiveData<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPricing(pricing: Pricing)

    // END ÜCRETLENDİRME

    // DUYURULAR
    @Query("Select * from Duyurular")
    fun getDuyurular(): LiveData<List<Duyurular>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDuyurular(duyuruList: List<Duyurular>)

    @Query("Delete from Duyurular")
    suspend fun removeDuyurular()

    @Transaction
    suspend fun insertDuyurular(duyuruList: List<Duyurular>) {
        removeDuyurular()
        addDuyurular(duyuruList)
    }
    // END DUYURULAR

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