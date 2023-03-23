package com.example.bmicalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.example.bmicalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { // 뷰 바인딩 객체 얻기
        // ActivityMainBinding 클래스는 activity_main.xml 파일의 이름을 참고하여 뷰 바인딩 설정에 의해서 자동으로 생성된 클래스
        // 이 객체를 통해서 activity_main.xml 파일에 정의한 뷰에 접근할 수 있음
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 바인딩 객체의 근원인 activity_main.xml 을 액티비티의 화면으로 인식하게 됨
        setContentView(binding.root) // 이 코드는 모든 액티비티를 만든 후 가장 먼저 선행해야 함

        loadData() // 액티비티를 시작하면 마지막에 저장한 값을 불러오기

        // 키와 몸무게 데이터 전달
        binding.resultButton.setOnClickListener {
            // 결과 버튼이 클릭되면 할 일을 작성하는 부분
            if (binding.weightEditText.text.isNotBlank() && binding.heightEditText.text.isNotBlank()) { // 아무 입력도 하지 않았을 경우의 에러 처리
                // 마지막에 입력한 내용 저장
                saveData(
                    binding.heightEditText.text.toString().toFloat(),
                    binding.weightEditText.text.toString().toFloat(),
                )

                // 안드로이드 액티비티를 전환할 때마다 인텐트 객체를 생성하고 startActivity() 메서드를 호출
                val intent = Intent(this, ResultActivity::class.java).apply {
                    // 입력한 키와 몸무게로 입력받은 문자열을 Float형으로 변경하여 엔텐트에 데이터 담기
                    putExtra("weight", binding.weightEditText.text.toString().toFloat()) // putExtra() 메서드에 키와 값의 쌍으로 데이터를 저장
                    putExtra("height", binding.heightEditText.text.toString().toFloat())
                }
                startActivity(intent)
            }
        }
    }

    // 데이터 저장하기 메서드 추가
    private fun saveData(height: Float, weight: Float) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this) // 프리퍼런스 매니저를 통해 프리퍼런스 객체를 얻기
        val editor = pref.edit() // 프리퍼런스 객체의 에디터 객체를 얻기 -> 이 객체를 사용해 프리퍼런스에 데이터를 담을 수 있음

        editor.putFloat("KEY_HEIGHT", height) // 에디터 객체에 put[데이터 타입] 형식의 메서드를 사용하여 키와 값 형태의 쌍으로 저장을 함
            .putFloat("KEY_WEIGHT", weight)
            .apply() // 마지막으로 설정한 내용을 반영
    }

    // 데이터 불러오기 메서드 추가
    private fun loadData() {
        val pref = PreferenceManager.getDefaultSharedPreferences(this) // 프리퍼런스 객체를 얻기
        val height = pref.getFloat("KEY_HEIGHT", 0f) // getFloat() 메서드로 키와 몸무게에 저장된 값을 불러옴 / getFloat() 메서드의 두 번째 인자는 저장된 값이 없을 때 기본값 0f를 리턴한다는 의미
        val weight = pref.getFloat("KEY_WEIGHT", 0f)

        if (height != 0f && weight != 0f) { // 키와 몸무게가 모두 0f인 경우 즉, 저장된 값이 없을 때는 아무것도 하지 않음
            binding.heightEditText.setText(height.toString())
            binding.weightEditText.setText(weight.toString())
        }
    }
}