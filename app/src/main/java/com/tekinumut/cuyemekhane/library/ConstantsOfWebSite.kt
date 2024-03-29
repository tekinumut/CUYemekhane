package com.tekinumut.cuyemekhane.library

class ConstantsOfWebSite {
    companion object {
        // Kaynak URL
        const val SourceURL = "https://yemekhane.cu.edu.tr"

        // Ana Sayfa
        const val MainPageURL = "$SourceURL/default.asp"

        // Ücretlendirme Sayfası
        const val PricingURL = "$SourceURL/ucretlendirme.asp"

        // DUYURU SINIFI
        const val duyuruClass = "section#duyurular .testimonial-item"

        // DUYURU BAŞLIĞI
        const val duyuruTitle = "section#duyurular .testimonial-item p"

        // DUYURU İÇERİĞİ
        const val duyuruContent = "section#duyurular .testimonial-item >span"

        // Bilgisi girilen listelerin tarihlerini alır
        const val monthlyDates = ".call-to-action .col-md-2:has(ul) font:eq(1), .col-md-3:has(ul) font:eq(1)"

        // Bilgisi girilen listelerin yemeklerini alır
        const val monthlyFoods = ".call-to-action ul"

        // Günlük yemeklerin bulunduğu genel sınıf
        // Günlük yemeklerin sayısını almak için kullanılır
        const val dailyFoodGeneral = "#gunluk_yemek ul li"

        // Günün yemek tarihi
        const val dailyDate = "#gunluk_yemek h1"

        // Günün yemek kategorisi
        const val dailyCategories = "#gunluk_yemek ul li .tp-caption.FoodCarousel-Button.rev-btn>span"

        // Günün yemek başlığı
        const val dailyFoodName = "#gunluk_yemek ul li .tp-caption.FoodCarousel-Button.rev-btn"

        // Günün yemek kalorisi
        const val dailyFoodCalorie = "#gunluk_yemek ul li font"

    }
}