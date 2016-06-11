package yutakosuki.github.io.kotlinreversi

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

internal class ReversiView(context: Context) : View(context) {
    private var res: Resources = context.resources
    private var IMG_BOARD: Bitmap = BitmapFactory.decodeResource(res, R.drawable.board)
    private var IMG_BLACK: Bitmap = BitmapFactory.decodeResource(res, R.drawable.black)
    private var IMG_WHITE: Bitmap = BitmapFactory.decodeResource(res, R.drawable.white)
    private var IMG_LIGHT: Bitmap = BitmapFactory.decodeResource(res, R.drawable.light)

    private var paint: Paint = Paint()

    private var TITLE: Int = 0
    private var PLAYER: Int = 1
    private var COM: Int = 2
    private var TURN: Int = 3
    private var REVERS: Int = 4
    private var CONTROL: Int = 5
    private var PASS: Int = 6
    private var RESULT: Int = 7

    private val board: Array<Int> = Array(100) { 0 }
    private val page:Int = TITLE

    //描写処理
    public override fun onDraw(c: Canvas) {
        // ボードを表示
        c.drawBitmap(IMG_BOARD, 0.0f, 0.0f, paint)
        for(i in (11..88)) {
            if(board[i]==PLAYER) c.drawBitmap(IMG_BLACK, 48.0f*(i%10), 48.0f*(i/10), paint);
            if(board[i]==COM) c.drawBitmap(IMG_WHITE, 48.0f*(i%10), 48.0f*(i/10), paint);
        }

        when(page) {
            TITLE -> {}
            TURN -> {}
            PLAYER -> {}
            COM -> {}
            REVERS -> {}
            CONTROL -> {}
            PASS -> {}
            RESULT -> {}
        }
    }

    //タッチ入力処理
    override fun onTouchEvent(me: MotionEvent): Boolean {
        if (me.action == MotionEvent.ACTION_DOWN) {
            when(page) {
                TITLE -> {}
                PLAYER -> {}
                COM -> {}
                PASS -> {}
                RESULT -> {}
            }
        }
        return true
    }
}