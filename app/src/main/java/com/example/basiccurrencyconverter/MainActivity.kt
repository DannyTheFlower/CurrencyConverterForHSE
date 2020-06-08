package com.example.basiccurrencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currencyData = arrayOf("Доллар", "Евро", "Рубль", "Фунт", "Гривна")
        val currencyValues = arrayOf(1f, 1.13f, 0.015f, 1.27f, 0.038f) //значение валют в долларах США

        fun checkInputError(str: String): Boolean { //защита от пустых строк или чего ещё (на всякий случай)
            try {
                str.toFloat()
            } catch (e: NumberFormatException) {
                return false
            }
            return true
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyData)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = adapter
        spinner2.adapter = adapter

        var currency1 = 1f
        var currency2 = 1f

        var flag = true //защита от зацикливания текствотчеров

        fun convert(valueText1: EditText, valueText2: EditText, const1: Float, const2: Float) {
            val valueString = valueText1.text.toString()
            if (checkInputError(valueString) and flag) {
                val value = valueString.toFloat()
                flag = false
                val result = value * const1 / const2
                valueText2.setText(result.toString())
                valueText2.setSelection(valueText2.length())
                flag = true
            }
        }

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                currency1 = currencyValues[position]
                convert(value2, value1, currency2, currency1)
            }
        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                currency2 = currencyValues[position]
                convert(value1, value2, currency1, currency2)
            }
        }

        value1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                convert(value1, value2, currency1, currency2)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        value2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                convert(value2, value1, currency2, currency1)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
    }
}