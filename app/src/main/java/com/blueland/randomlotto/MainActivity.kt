package com.blueland.randomlotto

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.blueland.randomlotto.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { DataBindingUtil.setContentView(this, R.layout.activity_main) }

    private val tvNumList: List<TextView> by lazy {
        listOf(
            binding.tvNum0,
            binding.tvNum1,
            binding.tvNum2,
            binding.tvNum3,
            binding.tvNum4,
            binding.tvNum5
        )
    }
    private var didRun = false
    private val pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {

            numberPicker.minValue = 1
            numberPicker.maxValue = 45

        }
        initListener()
    }

    private fun initListener() {
        binding.apply {

            btnAdd.setOnClickListener {

                if (didRun) {
                    Toast.makeText(this@MainActivity, "초기화 후 시도해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (pickNumberSet.size >= 5 ) {
                    Toast.makeText(this@MainActivity, "번호는 5개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (pickNumberSet.contains(numberPicker.value)) {
                    Toast.makeText(this@MainActivity, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val textView = tvNumList[pickNumberSet.size]
                textView.text = numberPicker.value.toString()
                textView.isVisible = true

                setNumberBackground(numberPicker.value, textView)

                pickNumberSet.add(numberPicker.value)
            }

            btnRun.setOnClickListener {
                val list = getRandomNumber()

                list.forEachIndexed { index, number ->
                    val textView = tvNumList[index]
                    textView.text = number.toString()
                    textView.isVisible = true

                    setNumberBackground(number, textView)
                }

                didRun = true
            }

            btnReset.setOnClickListener {
                pickNumberSet.clear()
                tvNumList.forEach {
                    it.isVisible = false
                }

                didRun = false
            }
        }
    }

    private fun setNumberBackground(number :Int, textView: TextView) {
        when(number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this@MainActivity, R.drawable.circle_yellow)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this@MainActivity, R.drawable.circle_blue)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this@MainActivity, R.drawable.circle_red)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this@MainActivity, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this@MainActivity, R.drawable.circle_green)
        }
    }

    private fun getRandomNumber(): List<Int> {

        val numberList = mutableListOf<Int>()
            .apply {
                for (i in 1..45) {
                    if (pickNumberSet.contains(i)) {
                        continue
                    }

                    this.add(i)
                }
            }

        numberList.shuffle()

        return pickNumberSet.toList() + numberList.subList(0, 6 - pickNumberSet.size).sorted()
    }
}