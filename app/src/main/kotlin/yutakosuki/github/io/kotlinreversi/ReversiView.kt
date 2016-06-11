package yutakosuki.github.io.kotlinreversi

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View

internal class ReversiView(context: Context) : View(context) {

    //描写処理
    public override fun onDraw(c: Canvas) {
    }

    //タッチ入力処理
    override fun onTouchEvent(me: MotionEvent): Boolean {
        return true
    }
}