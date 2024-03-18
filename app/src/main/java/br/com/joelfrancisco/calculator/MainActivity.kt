package br.com.joelfrancisco.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var expression: TextView
    private lateinit var result: TextView
    private val possibleOperations = listOf('+', '-', '/', '*')

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        expression = findViewById(R.id.textViewExpression)
        result = findViewById(R.id.textViewResult)
    }

    fun handleClick(view: View) {
        val button = view as Button

        if (button.text[0] in possibleOperations && possibleOperations.any { expression.text.contains(it) }) {
            return
        }

        expression.append(button.text)
    }

    fun parseExpression(view: View) {
        var resultAccumulator = 0.0

        for (i in 0..<expression.text.length) {
            if (expression.text[i] == '*' || expression.text[i] == '/') {
                val (number1, number2) = getNumbers(i)

                resultAccumulator += if (expression.text[i] == '*') {
                    number1 * number2
                } else {
                    number1 / number2
                }
            }
        }


        for (i in 0..<expression.text.length) {
            if (expression.text[i] == '+') {
                val (number1, number2) = getNumbers(i)
                resultAccumulator += number1 + number2
            } else if (expression.text[i] == '-') {
                val (number1, number2) = getNumbers(i)
                resultAccumulator += number1 - number2
            }
        }

        if (result.text.length > 0) {
            return
        }

        result.append(resultAccumulator.toString())
    }

    private fun getNumbers(i: Int): Array<Float> {
        var temp = i - 1
        var number = "";
        while (expression.text[temp] !in possibleOperations) {
            number += expression.text[temp]

            if (temp == 0) {
                break
            }

            temp--
        }

        val number1 = number.reversed().toFloat()

        temp = i + 1
        number = ""

        while (expression.text[temp] !in possibleOperations) {
            number += expression.text[temp]

            if (temp == expression.text.length - 1) {
                break
            }

            temp++
        }

        val number2 = number.toFloat()

        return arrayOf(number1, number2)
    }

    fun handleClean(view: View) {
        expression.text = ""
        result.text = ""
    }
}