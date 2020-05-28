package com.tekinumut.cuyemekhane.library

import com.tekinumut.cuyemekhane.models.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.net.URL

class DataUtility {

    companion object {
        fun getMonthlyList(doc: Document): ListOfAll {
            // Tarih listesi -- Bir index örneği : 11.05.2020 Pazartesi
            val dates: Elements = doc.select(ConstantsOfWebSite.monthlyDates)

            // Tarihleri liste biçimine çevirir
            val foodDateList: List<FoodDate> = dates.mapIndexed { i, it -> FoodDate(i + 1, it.text()) }
            // Yemkleri liste biçiminde tutar
            val foodList: List<Food> = getMonthlyFoodListOfDate(doc, foodDateList)

            val detailAndComponentPair = getDetailAndCompOfFood(foodList)
            // Her yemeğe ait detay bilgi
            val foodDetail: List<FoodDetail> = detailAndComponentPair.first
            // Yemeği oluşturan bileşenleri tutar.
            val foodComponent: List<FoodComponent> = detailAndComponentPair.second

            return ListOfAll(foodDateList, foodList, foodDetail, foodComponent)
        }

        fun getDailyList(doc: Document): ListOfAll {
            // Tarih listesi -- Bir index örneği : 11.05.2020 Pazartesi
            // Liste her zaman 1 boyutlu olacak
            val todayDate = doc.select(ConstantsOfWebSite.dailyDate).map { FoodDate(1, it.text()) }
            // Yemekleri liste biçiminde tut
            val todayFoodList: List<Food> = getDailyFoodListOfDate(doc)

            val detailAndComponentPair = getDetailAndCompOfFood(todayFoodList)
            // Her yemeğe ait detay bilgi
            val foodDetail: List<FoodDetail> = detailAndComponentPair.first
            // Yemeği oluşturan bileşenleri tutar.
            val foodComponent: List<FoodComponent> = detailAndComponentPair.second

            return ListOfAll(todayDate, todayFoodList, foodDetail, foodComponent)
        }

        /**
         * Verilerin tarihe ait yemekleri dönderir
         * Günün yemeği burada ele alınır.
         */
        private fun getDailyFoodListOfDate(doc: Document): List<Food> {
            val foodList = ArrayList<Food>()
            val foodGeneral: Elements = doc.select(ConstantsOfWebSite.dailyFoodGeneral)
            val foodCategory: Elements = doc.select(ConstantsOfWebSite.dailyCategories)
            val foodName: Elements = doc.select(ConstantsOfWebSite.dailyFoodName)
            val foodCalorie: Elements = doc.select(ConstantsOfWebSite.dailyFoodCalorie)

            for (i in 0 until foodGeneral.size) {
                val foodDetailURL = foodGeneral[i].select("[href]").attr("abs:href")
                foodList.add(Food(i + 1,
                    foodCategory[i].text(),
                    foodName[i].text(),
                    foodCalorie[i].text(),
                    foodDetailURL,
                    1
                ))
            }
            return foodList.toList()
        }

        /**
         * Verilen tarihlere ait yemek listesi oluşturur
         * Aylık yemek listesi alınır.
         */
        private fun getMonthlyFoodListOfDate(doc: Document, foodDateList: List<FoodDate>): List<Food> {
            // Yemek listesi -- Örn: Kıymalı Sandviç 602 Kalori Şalgam 20 Kalori Meyve (Elma) 142 Kalori
            val yemekler: Elements = doc.select(ConstantsOfWebSite.monthlyFoods)

            val foodList = ArrayList<Food>()
            var index = 0
            // Her tarih için çalış
            foodDateList.forEachIndexed { i, tarih ->
                val yemek = yemekler[i].select("li")
                // Eğer yemek bilgisi girilmemiş ise
                if (yemek.size == 0) {
                    index += 1
                    foodList.add(Food(food_id = index, name = yemekler[i].text(), dateCreatorId = tarih.date_id))
                } else {
                    yemek.forEach {
                        index += 1
                        val foodNameCal = getYemekWithCalorie(it)
                        val urlOfFood = it.select("[href]").attr("abs:href")
                        foodList.add(Food(index, null, foodNameCal.first, foodNameCal.second, urlOfFood, tarih.date_id))
                    }
                }
            }
            return foodList.toList()
        }

        /**
         * Yemek listesinden yemek detaylarını ve bileşenlerini dönderir
         */
        private fun getDetailAndCompOfFood(foodList: List<Food>): Pair<List<FoodDetail>, List<FoodComponent>> {
            val detailList = ArrayList<FoodDetail>()
            val componentList = ArrayList<FoodComponent>()
            foodList.forEach { yModel ->
                yModel.detailURL?.let { url ->
                    val docDetail = Jsoup.parse(URL(url).openStream(), "windows-1254", url)
                    // Türkçe karakter sorununu giderir
                    docDetail.outputSettings().charset("windows-1254")
                    val foodImgURL: String = docDetail.select("[src]").attr("abs:src")
                    val details: List<String> = docDetail.select("td").map { it.text().toString() }
                    details.forEach { componentList.add(FoodComponent(null, it, yModel.food_id)) }
                    detailList.add(FoodDetail(yModel.food_id, Utility.imgURLToBase64(foodImgURL)))
                }
            }
            return Pair(detailList, componentList)
        }

        /**
         * Gelen yemek bilgisini (Kıymalı Sandviç 600 Kalori)
         * yemek adı ve kalorisini ayırarak dönderir.
         * name = Kıymalı Sandviç  --  calorie = 600 Kalori
         */
        private fun getYemekWithCalorie(yemek: Element): Pair<String, String> {
            // br taglerini text'e çevirirken tutmak için $$$ ile değiştir.
            val replacedHtml = yemek.html().replace("<br>", "$$$")
            // Elimizde Kıymalı Sandviç<br>600 Kalori biçiminde bir text oldu
            val newHtml = Jsoup.parse(replacedHtml).text().replace("$$$", "<br>")
            val name = newHtml.substringBefore("<br>")
            val calorie = newHtml.substringAfter("<br>")
            return Pair(name, calorie)
        }


    }
}