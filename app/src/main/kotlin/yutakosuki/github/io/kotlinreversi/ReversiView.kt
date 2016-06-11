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
    private val res: Resources = context.resources
    private val IMG_BOARD: Bitmap = BitmapFactory.decodeResource(res, R.drawable.board)
    private val IMG_BLACK: Bitmap = BitmapFactory.decodeResource(res, R.drawable.black)
    private val IMG_WHITE: Bitmap = BitmapFactory.decodeResource(res, R.drawable.white)
    private val IMG_LIGHT: Bitmap = BitmapFactory.decodeResource(res, R.drawable.light)

    private val paint: Paint = Paint()

    private val TITLE: Int = 0
    private val PLAYER: Int = 1
    private val COM: Int = 2
    private val TURN: Int = 3
    private val REVERS: Int = 4
    private val CONTROL: Int = 5
    private val PASS: Int = 6
    private val RESULT: Int = 7

    private var board: Array<Int> = Array(100) { 0 }
    private var page: Int = TITLE
    private var turn: Int = TITLE

    //描写処理
    public override fun onDraw(c: Canvas) {
        // ボードを表示
        c.drawBitmap(IMG_BOARD, 0.0f, 0.0f, paint)
        for (i in (11..88)) {
            if (board[i] == PLAYER) c.drawBitmap(IMG_BLACK, 48.0f * (i % 10), 48.0f * (i / 10), paint);
            if (board[i] == COM) c.drawBitmap(IMG_WHITE, 48.0f * (i % 10), 48.0f * (i / 10), paint);
        }

        when (page) {
            TITLE -> {
            }
            TURN -> {
                // ページ移動
                page = TURN
                invalidate()
            }
            PLAYER -> {
            }
            COM -> {
            }
            REVERS -> {
                // ページ移動
                page = CONTROL
                invalidate()
            }
            CONTROL -> {
                // ターン交代
                turn = if (turn == PLAYER) COM else PLAYER
                page = TURN
                invalidate()
            }
            PASS -> {
            }
            RESULT -> {
            }
        }
    }

    //タッチ入力処理
    override fun onTouchEvent(me: MotionEvent): Boolean {
        if (me.action == MotionEvent.ACTION_DOWN) {
            when (page) {
                TITLE -> {
                    for (i in (0..100)) {
                        board[i] = 0
                    }
                    for (i in (0..10)) {
                        board[i] = -1
                        board[i + 90] = -1
                    }
                    for (i in (1..9)) {
                        board[i * 10] = -1
                        board[i * 10 + 9] = -1
                    }
                    board[44] = COM
                    board[45] = PLAYER
                    board[54] = PLAYER
                    board[55] = COM
                    turn = PLAYER
                    //ページ移動
                    page = turn
                    invalidate()
                }
                PLAYER -> {
                    //ページ移動
                    page = REVERS
                    invalidate()
                }
                COM -> {
                    //ページ移動
                    page = REVERS
                    invalidate()
                }
                PASS -> {
                }
                RESULT -> {
                }
            }
        }
        return true
    }
}