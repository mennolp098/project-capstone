package com.example.capstoneproject.utils

import android.content.res.Resources
import com.example.capstoneproject.R
import kotlin.random.Random

class ColorUtils() {
    fun getRandomAndroidColor(resources: Resources): Int {
        val androidColors: IntArray = resources.getIntArray(R.array.playercolors)
        return androidColors[Random.nextInt(androidColors.size)]
    }
}