package com.example.bmicalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bmicalculator.databinding.ActivityResultBinding
import kotlin.math.pow

class ResultActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityResultBinding.inflate((layoutInflater))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 전달받은 키와 뭄무게
        val height = intent.getFloatExtra("height", 0f) // 데이터가 담긴 인텐트에서 데이터를 꺼내려면 getXXXExtra() 메서드를 사용
        val weight = intent.getFloatExtra("weight", 0f)

        // Bmi 계산
        val bmi = weight / (height / 100.0f).pow(2.0f)

        // 결과 표시
        when {
            bmi >= 35 -> binding.resultTextView.text = "고도 비만"
            bmi >= 30 -> binding.resultTextView.text = "2단계 비만"
            bmi >= 25 -> binding.resultTextView.text = "1단계 비만"
            bmi >= 23 -> binding.resultTextView.text = "과체중"
            bmi >= 18.5 -> binding.resultTextView.text = "정상"
            else -> binding.resultTextView.text = "저체중"
        }

        // 이미지 표시
        when {
            bmi >= 23 ->
                binding.imageView.setImageResource(
                    R.drawable.ic_baseline_sentiment_very_dissatisfied_24)
            bmi >= 18.5 ->
                binding.imageView.setImageResource(
                    R.drawable.ic_baseline_sentiment_satisfied_alt_24)
            else ->
                binding.imageView.setImageResource(
                    R.drawable.ic_baseline_sentiment_dissatisfied_24)
        }

        // BMI값을 표시하는 토스트 코드
        Toast.makeText(this, "$bmi", Toast.LENGTH_SHORT).show() // BMI값이 Float형이므로 $ 기호를 사용하여 문자열 템플릿에 적용

    }
}