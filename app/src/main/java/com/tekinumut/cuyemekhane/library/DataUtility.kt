package com.tekinumut.cuyemekhane.library

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.tekinumut.cuyemekhane.RetroMainApi
import com.tekinumut.cuyemekhane.models.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.ByteArrayOutputStream

class DataUtility {

    companion object {

        private var retroMainApi: RetroMainApi = RetroMainApi.builder()

        /**
         * Verilen dökümandan aylık yemek verilerini alır
         */
        fun getMonthlyList(doc: Document, isDlImage: Boolean): ListOfAll {
            // Tarih listesi -- Bir index örneği : 11.05.2020 Pazartesi
            val dates: Elements = doc.select(ConstantsOfWebSite.monthlyDates)

            // Tarihleri liste biçimine çevirir
            val foodDateList: List<FoodDate> = dates.mapIndexed { i, it -> FoodDate(i + 1, it.text()) }
            // Yemkleri liste biçiminde tutar
            val foodList: List<Food> = getMonthlyFoodListOfDate(doc, foodDateList)
            val detailAndComponentPair = getDetailAndCompOfFood(foodList, isDlImage, false)
            // Her yemeğe ait detay bilgi
            val foodDetail: List<FoodDetail> = detailAndComponentPair.first
            // Yemeği oluşturan bileşenleri tutar.
            val foodComponent: List<FoodComponent> = detailAndComponentPair.second

            return ListOfAll(foodDateList, foodList, foodDetail, foodComponent)
        }

        /**
         *  Verilen dökümandan günlük yemek verilerini alır
         */
        fun getDailyList(doc: Document, isDlImage: Boolean): ListOfAll {
            // Tarih listesi -- Bir index örneği : 11.05.2020 Pazartesi
            // Liste her zaman 1 boyutlu olacak
            val todayDate = doc.select(ConstantsOfWebSite.dailyDate).map { FoodDate(1, it.text()) }
            // Yemekleri liste biçiminde tut
            val todayFoodList: List<Food> = getDailyFoodListOfDate(doc)
            val detailAndComponentPair = getDetailAndCompOfFood(todayFoodList, isDlImage, true)
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
                val foodDetailURL = ConstantsOfWebSite.SourceURL + "/" + foodGeneral[i].attr("data-thumb")
                val foodDetailCalorie = if (!foodCalorie[i].ownText().isNullOrEmpty()) {
                    foodCalorie[i].ownText() + " Kalori"
                } else null
                foodList.add(
                    Food(
                        i + 1,
                        foodCategory.getOrNull(i)?.text() ?: "",
                        foodName[i].ownText(),
                        foodDetailCalorie,
                        foodDetailURL,
                        1
                    )
                )
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
         * Aylık-Günlük liste verilerini alırken her url'yi asenkron çalıştırmak için async kullanılıyor.
         * Böylece 30 yemeğin verisi aynı anda çekiliyor.
         */
        private fun getDetailAndCompOfFood(foodList: List<Food>, isDlImage: Boolean, isDailyFood: Boolean): Pair<List<FoodDetail>, List<FoodComponent>> =
            runBlocking {
                val detailList = ArrayList<Deferred<FoodDetail>>()
                val componentList = ArrayList<FoodComponent>()
                foodList.forEach { yModel ->
                    if (!yModel.detailURL.isNullOrEmpty()) {
                        // Her isteğin asenkron çalışması için hepsini async içerisinde çalıştırıyoruz
                        // Daha sonra tüm çalışan isteklerin bitmesini awaitAll ile beklicez
                        detailList.add(async {
                            // Adresin html bilgisini çek
                            val htmlCode = retroMainApi.getStringOfURL(yModel.detailURL).body()
                            // Jsoup ile parse edip Document dosyasına dönüştür
                            val docDetail = Jsoup.parse(htmlCode, ConstantsOfWebSite.MainPageURL)
                            // Yemek resim adresini çek
                            val foodImgURL: String? = docDetail.select("[src]").attr("abs:src")
                            // Yemeğin bileşenlerini çek
                            val details: List<String> = docDetail.select("td").map { it.text().toString() }
                            // Tüm bileşenleri tek tek listeye ekle
                            details.forEach { componentList.add(FoodComponent(null, it, yModel.food_id)) }
                            // Yemeğin resmini base64 olarak bağlanılan siteden çek
                            FoodDetail(yModel.food_id, getImageOfUrl(isDlImage, foodImgURL))
                        }
                        )
                    }
                }
                Pair(detailList.awaitAll(), componentList)
            }

        /**
         * İçerisinde resim bulunan URL'yi base64 formatına çevirir.
         */
        private suspend fun getImageOfUrl(isDlImage: Boolean, foodImgURL: String?): String? {
            return if (!isDlImage || foodImgURL.isNullOrEmpty()) {
                null
            } else {
                val input = retroMainApi.getImageByte(foodImgURL).body()?.byteStream()
                val bitmap = BitmapFactory.decodeStream(input)
                val baos = ByteArrayOutputStream()
                // CompressFormat PNG olursa quality'i dikkate almaz.
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                val b = baos.toByteArray()
                return Base64.encodeToString(b, Base64.DEFAULT)
            }
        }

        /**
         * Gelen yemek bilgisini (Kıymalı Sandviç 600 Kalori)
         * yemek adı ve kalorisini ayırarak dönderir.
         * name = Kıymalı Sandviç  --  calorie = 600 Kalori
         */
        private fun getYemekWithCalorie(yemek: Element): Pair<String, String> {
            // br taglerini text'e çevirirken tutmak için $$$ ile değiştir.
            // Elimizde Kıymalı Sandviç$$$600 Kalori biçiminde bir text oldu
            val replacedHtml = yemek.html().replace("<br>", "$$$")
            // Şimdi Kıymalı Sandviç<br>600 Kalori olarka çevirdik
            val newHtml = Jsoup.parse(replacedHtml).text().replace("$$$", "<br>")
            // <br> tag öncesini alıyoruz. - Kıymalı Sandviç
            val name = newHtml.substringBefore("<br>")
            // <br> tag sonrası 600 Kalori
            val calorie = newHtml.substringAfter("<br>")
            return Pair(name, calorie)
        }


    }
}