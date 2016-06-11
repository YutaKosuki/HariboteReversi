package yutakosuki.github.io.kotlinreversi

import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout

class KotlinReversi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 画面を縦方向で固定する
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val linearLayout = LinearLayout(this)
        setContentView(linearLayout)

        linearLayout.addView(ReversiView(this))
    }
}
