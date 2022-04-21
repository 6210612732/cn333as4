package com.example.cn333_as4.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cn333_as4.R

class WeightViewModel: ViewModel() {
    private val _wei: MutableLiveData<Int> = MutableLiveData(R.string.kilo)

    val wei: LiveData<Int>
        get() = _wei

    fun setWei(value: Int) {
        _wei.value = value
    }

    private val _weight: MutableLiveData<String> = MutableLiveData("")

    val weight: LiveData<String>
        get() = _weight

    fun getWeightAsFloat(): Float = (_weight.value ?: "").let {
        return try {
            it.toFloat()
        } catch (e: NumberFormatException) {
            Float.NaN
        }
    }

    fun setWeight(value: String) {
        _weight.value = value
    }

    fun convert() = getWeightAsFloat().let {
        if (!it.isNaN())
            if (_wei.value == R.string.kilo)
                it * 2.20462262F
            else
                it * 0.45359237F
        else
            Float.NaN
    }
}

