package com.tekinumut.cuyemekhane

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.tekinumut.cuyemekhane.library.ConstantsGeneral
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvEkleDaily.setOnClickListener {
            mainViewModel.getFoodData(ConstantsGeneral.dbNameDaily).observe(this, Observer {
                Log.e("new daily value", "value added")
            })
        }

        tvEkleMonthly.setOnClickListener {
            mainViewModel.getFoodData(ConstantsGeneral.dbNameMonthly).observe(this, Observer {
                Log.e("new monthly value", "value added")
            })
        }

        tvGoruntule.setOnClickListener {
            mainViewModel.getDailyList.observe(this, Observer {
                if (it != null) {
                    Log.e("daily list changed", "new date : ${it.foodDate.name}")
                } else {
                    Log.e("daily list opened", "list is null")
                }

            })
            mainViewModel.getMonthlyList.observe(this, Observer {
                Log.e("monthly list changed", "food size : ${it.size}")
            })
        }
    }
}
