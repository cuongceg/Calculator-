package com.example.mycalculator

import android.icu.text.DecimalFormat
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private var currentInput: String = ""
    private var operator: String? = null
    private var operand1: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_linear)
        display = findViewById(R.id.display)
        val buttons = listOf(
            R.id.zero, R.id.one, R.id.two, R.id.three, R.id.four,
            R.id.five, R.id.six, R.id.seven, R.id.eight, R.id.nine,
            R.id.dot, R.id.plus, R.id.minus, R.id.multiply, R.id.Divide,
            R.id.equal, R.id.C, R.id.CE, R.id.BS, R.id.positive
        )

        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { onButtonClick(it as Button) }
        }
    }

    private fun onButtonClick(button: Button) {
        when (val text = button.text.toString()) {
            in "0".."9", "." -> appendToInput(text)
            "+", "-", "x", "/" -> setOperator(text)
            "=" -> calculateResult()
            "C" -> clearAll()
            "CE" -> clearEntry()
            "BS" -> backspace()
            "+/-" -> toggleSign()
        }
    }

    private fun appendToInput(text: String) {
        currentInput += text
        display.text = currentInput
    }

    private fun setOperator(op: String) {
        operand1 = currentInput.toDoubleOrNull()
        operator = op
        currentInput = ""
        display.text = op
    }

    private fun calculateResult() {
        val operand2 = currentInput.toDoubleOrNull()
        if (operand1 != null && operand2 != null && operator != null) {
            val result= when (operator) {
                "+" -> operand1!! + operand2
                "-" -> operand1!! - operand2
                "x" -> operand1!! * operand2
                "/" -> operand1!! / operand2
                else -> 0.0
            }
            display.text = if (result % 1 == 0.0) {
                result.toLong().toString()
            } else {
                formatNumber(result)
            }
            currentInput = result.toString()
            operator = null
            operand1 = null
        }
    }

    private fun formatNumber(number: Double): String {
        val decimalFormat = DecimalFormat("#.########")

        return decimalFormat.format(round(number * 1_000_000_0) / 1_000_000_0)
    }

    private fun clearAll() {
        currentInput = ""
        operator = null
        operand1 = null
        display.text = getText(R.string.default_value)
    }

    private fun clearEntry() {
        currentInput = ""
        display.text = getText(R.string.default_value)
    }

    private fun backspace() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            display.text = currentInput
        }
    }

    private fun toggleSign() {
        if (currentInput.isNotEmpty()) {
            currentInput = if (currentInput.startsWith("-")) {
                currentInput.drop(1)
            } else {
                "-$currentInput"
            }
            val number = currentInput.toDouble()
            display.text = if(number % 1 == 0.0){
                number.toInt().toString()
            } else {
                formatNumber(number)
            }
        }
    }
}