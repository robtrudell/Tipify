package com.robtrudell.tipify

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "mainAcivity"
private const val INITIAL_TIP = 15
class MainActivity : AppCompatActivity() {
    private lateinit var etBase: EditText
    private lateinit var seekPercent: SeekBar
    private lateinit var tvPercent: TextView
    private lateinit var tvTip: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvRate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etBase = findViewById(R.id.etBase)
        seekPercent = findViewById(R.id.seekPercent)
        tvPercent = findViewById(R.id.tvPercent)
        tvTip = findViewById(R.id.tvTip)
        tvTotal = findViewById(R.id.tvTotal)
        tvRate = findViewById(R.id.tvRate)

        seekPercent.progress = INITIAL_TIP
        tvPercent.text = "$INITIAL_TIP%"
        updateRate(INITIAL_TIP)
        seekPercent.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvPercent.text = "$progress%"
                computeTipAndTotal()
                updateRate(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
        etBase.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG,"afterTextChanged $s")
                computeTipAndTotal()
            }
        })
    }

    private fun updateRate(tipPercent: Int) {
        val tipRate = when (tipPercent) {
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"

        }
        tvRate.text = tipRate
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekPercent.max,
            ContextCompat.getColor(this,R.color.worst),
            ContextCompat.getColor(this,R.color.best)
            ) as Int
        tvRate.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if(etBase.text.isEmpty()) {
            tvTip.text = ""
            tvTotal.text = ""
            return
        }
        val baseAmount = etBase.text.toString().toDouble()
        val tipPercent = seekPercent.progress
        val tipAmount =baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount
        tvTip.text = "%.2f".format(tipAmount)
        tvTotal.text = "%.2f".format(totalAmount)
    }
}