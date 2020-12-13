package com.example.capstoneproject.utils

class ValueStepsUtil {
    fun getValueStepsFromAmount(max: Int): Array<Int> {
        return when {
            max <= 100 -> {
                arrayOf(-25,-10,-1,1,10,25)
            }
            max <= 250 -> {
                arrayOf(-50,-25,-10,10,25,50)
            }
            max <= 500 -> {
                arrayOf(-100,-50,-25,25,50,100)
            }
            else -> arrayOf(-500, -250, -50, 50, 250, 500)
        }
    }
}