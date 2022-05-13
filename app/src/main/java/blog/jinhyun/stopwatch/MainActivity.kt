package blog.jinhyun.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import blog.jinhyun.stopwatch.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    /**
     * `timerTask`의 시간 변수
     */
    private var time = 0

    /**
     * 타이머 객체
     */
    private var timerTask: Timer? = null

    /**
     * 타이머 실행 여부
     */
    private var timerIsRunning = false

    private var lapNumber = 0

    /**
     * 뷰 바인딩
     */
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // `play` 버튼 이벤트
        binding.floatingActionButtonPlay.setOnClickListener {
            // 타이머의 실행 상태 반전
            timerIsRunning = !timerIsRunning

            if (timerIsRunning) {
                startTimer()
            } else {
                pauseTimer()
            }
        }

        // `reset` 버튼 이벤트
        binding.floatingActionButtonReset.setOnClickListener {
            reset()
        }

        binding.buttonLaptime.setOnClickListener {
            record()
        }
    }

    /**
     * `refresh` 버튼을 누르면 작동하는 함수.
     * 타이머를 취소하고, 시간과 타이머 상태를 초기화.
     */
    private fun reset() {
        timerTask?.cancel()
        time = 0
        timerIsRunning = false
        binding.floatingActionButtonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        refreshView(0, 0)
        binding.layoutLaptime.removeAllViews()
        lapNumber = 0
    }

    /**
     * play 버튼을 누르면 수행되는 타이머 카운트 함수
     */
    private fun startTimer() {
        // `play`버튼의 `play` 이미지를 `pause` 이미지로 변경
        binding.floatingActionButtonPlay.setImageResource(R.drawable.ic_baseline_pause_24)

        // 0.01 초마다 수행
        timerTask = timer(period = 10) {
            // 연산
            time++
            val second = getSecond()
            val millisecond  = getMillisecond()
            // UI 조작
            runOnUiThread {
                refreshView(second, millisecond)
            }
        }
    }

    /**
     * 타이머를 중지하는 함수
     */
    private fun pauseTimer() {
        // 버튼의 이미지 변경 `pause` -> `play`
        binding.floatingActionButtonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        // 실행중인 타이머 취소
        timerTask?.cancel()
    }

    /**
     * Second, Millisecond 뷰의 텍스트를 갱신하는 함수
     */
    private fun refreshView(second: Int, millisecond: Int) {
        binding.textViewSeconds.text = second.toString()
        binding.textViewMilliSeconds.text = if(millisecond != 0) {
            millisecond.toString()
        } else {
            "00"
        }
    }

    /**
     * 랩 타임을 기록하는 함수
     */
    private fun record() {
        lapNumber++
        val textViewLaptime = TextView(this)
        textViewLaptime.text = "[$lapNumber] LAP: ${getSecond()}.${getMillisecond()}"
        binding.layoutLaptime.addView(textViewLaptime, 0)
    }

    /**
     * 초를 반환하는 함수
     * @return time / 100
     */
    private fun getSecond(): Int = time / 100

    /**
     * 밀리초를 반환하는 함수
     * @return time % 100
     */
    private fun getMillisecond(): Int = time % 100
}