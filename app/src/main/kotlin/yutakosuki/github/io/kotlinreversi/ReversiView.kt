package yutakosuki.github.io.kotlinreversi

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.service.voice.AlwaysOnHotwordDetector
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

    private val MOVE: Array<Int> = arrayOf(-11, -10, -9, -1, 1, 9, 10, 11)

    private val BLOCK_WIDTH: Int = 96
    private val BLOCK_HELGHT: Int = 96

    private var board: Array<Int> = Array(100) { 0 }
    private var page: Int = TITLE
    private var turn: Int = TITLE
    private var place: Int = 0
    private var placeMap: Array<Int> = Array(100) { 0 }

    //描写処理
    public override fun onDraw(c: Canvas) {
        // ボードを表示
        c.drawBitmap(IMG_BOARD, 0.0f, 0.0f, paint)
        for (i in (11..88)) {
            if (board[i] == PLAYER) c.drawBitmap(IMG_BLACK, (BLOCK_WIDTH * (i % 10)).toFloat(), (BLOCK_HELGHT * (i / 10)).toFloat(), paint);
            if (board[i] == COM) c.drawBitmap(IMG_WHITE, (BLOCK_WIDTH * (i % 10)).toFloat(), (BLOCK_HELGHT * (i / 10)).toFloat(), paint);
        }

        when (page) {
            TITLE -> {
            }
            TURN -> {
                // ページ移動
                page = turn
                invalidate()
            }
            PLAYER -> {
                makePlaceMap(PLAYER)
                //おける場所を表示
                for (i in (11..88)) {
                    if (placeMap[i] > 0) c.drawBitmap(IMG_LIGHT, (BLOCK_WIDTH * (i % 10)).toFloat(), (BLOCK_HELGHT * (i / 10)).toFloat(), paint)
                }
            }
            COM -> {
                makePlaceMap(COM)
                //おける場所を表示
                for (i in (11..88)) {
                    if (placeMap[i] > 0) c.drawBitmap(IMG_LIGHT, (BLOCK_WIDTH * (i % 10)).toFloat(), (BLOCK_HELGHT * (i / 10)).toFloat(), paint)
                }
            }
            REVERS -> {
                // おいて裏返す
                this.reverse(turn, place)
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
        var padX = (me.getX() / BLOCK_WIDTH).toInt()
        var padY = (me.getY() / BLOCK_HELGHT).toInt()

        if (me.action == MotionEvent.ACTION_DOWN) {
            when (page) {
                TITLE -> {
                    for (i in (0..99)) {
                        board[i] = 0
                    }
                    for (i in (0..9)) {
                        board[i] = -1
                        board[i + 90] = -1
                    }
                    for (i in (1..8)) {
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
                    if (placeMap[padX + padY * 10] > 0) {
                        place = padX + padY * 10
                        //ページ移動
                        page = REVERS
                        invalidate()
                    }
                }
                COM -> {
                    if (placeMap[padX + padY * 10] > 0) {
                        place = padX + padY * 10
                        //ページ移動
                        page = REVERS
                        invalidate()
                    }
                }
                PASS -> {
                }
                RESULT -> {
                }
            }
        }
        return true
    }

    // おいて裏返す
    private fun reverse(myCoin: Int, place: Int) {
        var yourCoin = PLAYER
        if (myCoin == PLAYER) {
            yourCoin = COM
        }
        board[place] = myCoin
    }

    // そこにはおけるか
    private fun makePlaceMap(myCoin: Int): Boolean {
        var yourCoin = PLAYER
        var pass = true

        if (myCoin == PLAYER) yourCoin = COM

        for (p in (0..99)) {
            placeMap[p] = 0
            if (0 < p && p < 100 && board[p] == 0) {
                for (i in (0..7)) {
                    if (board[p + MOVE[i]] == yourCoin) {
                        for (j in (2..7)) {
                            if (board[p + MOVE[i] * j] == myCoin) {
                                placeMap[p] += j - 1
                                pass = false
                                break
                            } else if (board[p + MOVE[i] * j] == yourCoin) {
                            } else {
                                break
                            }
                        }
                    }
                }
            }
        }
        return pass
    }
}