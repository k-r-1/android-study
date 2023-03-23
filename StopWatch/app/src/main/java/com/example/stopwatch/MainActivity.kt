package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.stopwatch.databinding.ActivityMainBinding
import kotlinx.coroutines.NonCancellable.cancel
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private var time = 0 // 시간을 계산할 변수를 0으로 초기화 선언
    private var timerTask: Timer? = null // timerTask 변수를 null을 허용하는 Timer 타입으로 선언
    private var isRunning = false // FAB이 클릭되면 타이머가 동작 중인지 저장하는 변수
    private var lap = 1 // 몇 번째 랩인지를 표시하고자 변수 lap을 1로 초기화

    // 뷰 바인딩 설정
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 시작과 일시정지 이벤트 구현
        binding.fab.setOnClickListener {
            isRunning = !isRunning // 변수의 값을 반전시키고
            // 그 상태에 따라서 타이머를 시작 또는 일시정지시킴
            if (isRunning) {
                start()
            } else {
                pause()
            }
        }

        // 액티비티에 랩 타임 버튼에 이벤트를 연결
        binding.lapButton.setOnClickListener {
            recordLapTime()
        }

        // 초기화 버튼에 이벤트 연결
        binding.resetFab.setOnClickListener {
            reset()
        }
    }

    // 타이머 시작 메서드
    private fun start() {
        // 타이머를 시작하는 FAB를 누르면 FAB의 이미지를 일시정지 이미지로 변경
        binding.fab.setImageResource(R.drawable.ic_baseline_pause_24)
        // 나중에 timer를 취소하려면 timer를 실행하고 반환되는 Timer 객체를 변수에 저장해둘 필요가 있음
        timerTask = timer(period = 10) { // 0.01초마다 이 변수를
            time++ // 증가시킴
            val sec = time / 100
            val milli = time % 100
            // timer는 워커 스레드에서 동작하여 UI 조작이 불가능하므로 runOnUiThread로 감싸서 UI 조작이 가능하게 함
            runOnUiThread {  // UI를 갱신
                binding.setTextView.text = "$sec"
                binding.milliTextView.text = "$milli"
            }
        }
    }

    // 타이머 일시정지 메서드
    private fun pause() {
        // FAB를 클릭하면 FAB의 이미지를 시작 이미지로 교체
        binding.fab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        timerTask?.cancel() // 실행 중인 타이머가 있다면 타이머를 취소
    }

    private fun recordLapTime() {
        val lapTime = this.time // 랩 타임 버튼을 클릭하면 현재 시간을 지역 변수에 저장
        val textView = TextView(this) // 동적으로 TextView를 생성하여
        textView.text = "$lap Lap : ${lapTime / 100}.${lapTime % 100}" // 'l LAP : 5.35'와 같은 형태가 되도록 시간을 계산하여 문자열로 설정

        // 맨 위에 랩타임 추가
        binding.lapLayout.addView(textView, 0) // 텍스트 뷰를 LinearLayout의 맨 위에 추가하고
        lap++ // 다음을 위해 1만큼 증가
    }

    // 타이머 초기화 메서드
    private fun reset() {
        timerTask?.cancel() // 실행 중인 타이머가 있다면 취소하고

        // 모든 변수와 화면에 표시되는 모든 것을 초기화
        time = 0
        isRunning = false

        binding.fab.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        binding.setTextView.text = "0"
        binding.milliTextView.text = "00"

        // 모든 랩타임을 제거
        binding.lapLayout.removeAllViews()
        lap = 1
    }
}

