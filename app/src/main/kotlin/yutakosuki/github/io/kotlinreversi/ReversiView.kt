package yutakosuki.github.io.kotlinreversi

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.service.voice.AlwaysOnHotwordDetector
import android.view.MotionEvent
import android.view.View

internal class ReversiView(context: Context) : View(context) {
    private val res: Resources = context.resources
    private val IMG_BOARD: Bitmap = BitmapFactory.decodeResource(this.res, R.drawable.board)
    private val IMG_BLACK: Bitmap = BitmapFactory.decodeResource(this.res, R.drawable.black)
    private val IMG_WHITE: Bitmap = BitmapFactory.decodeResource(this.res, R.drawable.white)
    private val IMG_LIGHT: Bitmap = BitmapFactory.decodeResource(this.res, R.drawable.light)

    private val paint: Paint = Paint()

    private val TITLE: Int = 0
    private val PLAYER: Int = 1
    private val COM: Int = 2
    private val TURN: Int = 3
    private val REVERS: Int = 4
    private val CONTROL: Int = 5
    private val PASS: Int = 6
    private val RESULT: Int = 7
    private val BLACK: Int = 8
    private val WHITE: Int = 9

    private val MOVE: Array<Int> = arrayOf(-11, -10, -9, -1, 1, 9, 10, 11)

    private val BLOCK_WIDTH: Int = 96
    private val BLOCK_HEIGHT: Int = 96

    private var board: Array<Int> = Array(100) { 0 }
    private var page: Int = this.TITLE
    private var turn: Int = this.TITLE
    private var place: Int = 0
    private var placeMap: Array<Int> = Array(100) { 0 }
    private var playerColor = this.BLACK

    init {
        this.paint.color = Color.BLUE
        this.paint.textSize = 60f
    }

    //描写処理
    public override fun onDraw(c: Canvas) {
        // ボードを表示
        c.drawBitmap(this.IMG_BOARD, 0.0f, 0.0f, this.paint)

        for (i in (11..88)) {
            if (this.board[i] == this.PLAYER) c.drawBitmap(this.IMG_BLACK, (this.BLOCK_WIDTH * (i % 10)).toFloat(), (this.BLOCK_HEIGHT * (i / 10)).toFloat(), this.paint);
            if (this.board[i] == this.COM) c.drawBitmap(this.IMG_WHITE, (this.BLOCK_WIDTH * (i % 10)).toFloat(), (this.BLOCK_HEIGHT * (i / 10)).toFloat(), this.paint);
        }

        when (this.page) {
            this.TITLE -> {
            }
            this.TURN -> {
                // ページ移動
                this.page = this.turn
                invalidate()
            }
            this.PLAYER -> {
                this.makePlaceMap(this.PLAYER)
                //おける場所を表示
                for (i in (11..88)) {
                    if (this.placeMap[i] > 0) c.drawBitmap(this.IMG_LIGHT, (this.BLOCK_WIDTH * (i % 10)).toFloat(), (this.BLOCK_HEIGHT * (i / 10)).toFloat(), this.paint)
                }
            }
            this.COM -> {
                this.makePlaceMap(this.COM)
                //おける場所を表示
                for (i in (11..88)) {
                    if (this.placeMap[i] > 0) c.drawBitmap(this.IMG_LIGHT, (this.BLOCK_WIDTH * (i % 10)).toFloat(), (this.BLOCK_HEIGHT * (i / 10)).toFloat(), this.paint)
                }
            }
            this.REVERS -> {
                // おいて裏返す
                this.reverse(this.turn, this.place)
                // ページ移動
                this.page = this.CONTROL
                invalidate()
            }
            this.CONTROL -> {
                // ターン交代
                this.turn = if (this.turn == this.PLAYER) this.COM else this.PLAYER
                // ページ移動
                if (this.makePlaceMap(this.PLAYER) == true && this.makePlaceMap(this.COM) == true) {
                    this.page = this.RESULT
                } else if (this.makePlaceMap(this.turn) == true) {
                    this.page = this.PASS
                } else {
                    this.page = this.TURN
                }
                invalidate()
            }
            this.PASS -> {
                c.drawText("パス", 200.toFloat(), 600.toFloat(), this.paint)
                // ターンを交代
                this.turn = if (this.turn == this.PLAYER) this.COM else this.PLAYER
                // ページ移動
                this.page = this.TURN
                invalidate()
            }
            this.RESULT -> {
                c.drawText("結果", 400.toFloat(), 1100.toFloat(), this.paint)
                c.drawLine(100.toFloat(), 1120.toFloat(), 860.toFloat(), 1120.toFloat(), this.paint)
                c.drawText("黒　" + this.count(this.BLACK) + "　個", 200.toFloat(), 1300.toFloat(), this.paint)
                c.drawText("白　" + this.count(this.WHITE) + "　個", 200.toFloat(), 1400.toFloat(), this.paint)
            }
        }
    }

    //タッチ入力処理
    override fun onTouchEvent(me: MotionEvent): Boolean {
        var padX = (me.x / this.BLOCK_WIDTH).toInt()
        var padY = (me.y / this.BLOCK_HEIGHT).toInt()

        if (padX > 10) return true
        if (padY > 10) return true

        if (me.action == MotionEvent.ACTION_DOWN) {
            when (this.page) {
                this.TITLE -> {
                    for (i in (0..99)) {
                        this.board[i] = 0
                    }
                    for (i in (0..9)) {
                        this.board[i] = -1
                        this.board[i + 90] = -1
                    }
                    for (i in (1..8)) {
                        this.board[i * 10] = -1
                        this.board[i * 10 + 9] = -1
                    }
                    this.board[44] = this.COM
                    this.board[45] = this.PLAYER
                    this.board[54] = this.PLAYER
                    this.board[55] = this.COM
                    this.turn = this.PLAYER
                    //ページ移動
                    this.page = this.turn
                    invalidate()
                }
                this.PLAYER -> {
                    if (this.placeMap[padX + padY * 10] > 0) {
                        this.place = padX + padY * 10
                        //ページ移動
                        this.page = this.REVERS
                        invalidate()
                    }
                }
                this.COM -> {
                    if (this.placeMap[padX + padY * 10] > 0) {
                        this.place = padX + padY * 10
                        //ページ移動
                        this.page = this.REVERS
                        invalidate()
                    }
                }
                this.PASS -> {
                }
                this.RESULT -> {
                    // ページ移動
                    this.page = this.TITLE
                    invalidate()
                }
            }
        }
        return true
    }

    // おいて裏返す
    private fun reverse(myCoin: Int, place: Int) {
        var yourCoin = this.PLAYER
        if (myCoin == this.PLAYER) {
            yourCoin = this.COM
        }
        this.board[place] = myCoin

        for (i in (0..7)) {
            if (this.board[place + this.MOVE[i]] == yourCoin) {
                for (j in (2..7)) {
                    if (this.board[place + this.MOVE[i] * j] == myCoin) {
                        for (k in (1..j - 1)) {
                            this.board[place + this.MOVE[i] * k] = myCoin
                        }
                        break
                    } else if (this.board[place + this.MOVE[i] * j] == yourCoin) {

                    } else {
                        break
                    }
                }
            }
        }
    }

    // そこにはおけるか
    private fun makePlaceMap(myCoin: Int): Boolean {
        var yourCoin = this.PLAYER
        var pass = true

        if (myCoin == this.PLAYER) yourCoin = this.COM

        for (p in (0..99)) {
            this.placeMap[p] = 0
            if (0 < p && p < 100 && this.board[p] == 0) {
                for (i in (0..7)) {
                    if (this.board[p + this.MOVE[i]] == yourCoin) {
                        for (j in (2..7)) {
                            if (this.board[p + this.MOVE[i] * j] == myCoin) {
                                this.placeMap[p] += j - 1
                                pass = false
                                break
                            } else if (this.board[p + this.MOVE[i] * j] == yourCoin) {
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

    // 石の数を数える
    private fun count(color: Int): Int {
        var count = 0

        if (this.playerColor == color) {
            for (i in (0..99)) {
                if (this.board[i] == this.PLAYER) count++;
            }
        } else {
            for (i in (0..99)) {
                if (this.board[i] == this.COM) count++;
            }
        }
        return count
    }
}