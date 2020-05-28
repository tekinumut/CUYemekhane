package com.tekinumut.cuyemekhane.library

class ConstantsOfWebSite {
    companion object {
        // Kaynak URL
        const val URL = "https://yemekhane.cu.edu.tr/default.asp"

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
        const val dailyFoodGeneral = "#gunluk_yemek .post-entry"

        // Günün yemek tarihi
        const val dailyDate = "#gunluk_yemek div:not([class])"

        // Günün yemek kategorisi
        const val dailyCategories = "#gunluk_yemek .post-entry-meta-category"

        // Günün yemek başlığı
        const val dailyFoodName = "#gunluk_yemek .post-entry-meta-title center:eq(0)"

        // Günün yemek kalorisi
        const val dailyFoodCalorie ="#gunluk_yemek .post-entry-meta-title center:eq(1)"

    }
}