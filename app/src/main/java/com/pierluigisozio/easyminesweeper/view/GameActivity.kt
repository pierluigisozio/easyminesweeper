package com.pierluigisozio.easyminesweeper.view

import android.annotation.SuppressLint
import android.content.IntentSender.OnFinished
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierluigisozio.easyminesweeper.R
import com.pierluigisozio.easyminesweeper.model.Cell
import com.pierluigisozio.easyminesweeper.viewmodel.GameGridRecyclerAdapter
import com.pierluigisozio.easyminesweeper.viewmodel.GameService
import com.pierluigisozio.easyminesweeper.viewmodel.OnCellClickListener

class GameActivity : AppCompatActivity(), OnCellClickListener {

    companion object {
        const val TIMER_LENGTH : Long = 999000L
        const val BOMB_COUNT : Int = 10
        const val GRID_SIZE : Int = 10
    }

    private lateinit var gameGridRecyclerAdapter : GameGridRecyclerAdapter

    private lateinit var grid : RecyclerView
    private lateinit var smiley : TextView
    private lateinit var timer : TextView
    private lateinit var flag : TextView
    private lateinit var flagsLeft : TextView

    private lateinit var gameService : GameService

    private lateinit var countDownTimer: CountDownTimer

    private var secondsElapsed = 0

    private var timerStarted = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        grid = findViewById(R.id.activity_game_grid)
        grid.layoutManager = GridLayoutManager(this,10)

        timer = findViewById(R.id.activity_game_timer)

        //in kotlin the keyword "object" is used to create an instance of anonymous class
        countDownTimer = object : CountDownTimer(TIMER_LENGTH, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsElapsed +=1
                timer.text = String.format("%03d", secondsElapsed)
            }

            override fun onFinish() {
                gameService.outOfTime()
                Toast.makeText(applicationContext, "Game Over: Timer Expired", Toast.LENGTH_SHORT).show()
                gameService.getGameGrid().revealAllBombs()
                gameGridRecyclerAdapter.setCells(gameService.getGameGrid().getCells())
            }
        }

        flagsLeft = findViewById(R.id.activity_game_flagsleft)

        gameService = GameService(GRID_SIZE, BOMB_COUNT)

        flagsLeft.text = String.format("%03d", gameService.getNumberBombs() - gameService.getFlagCount())

        gameGridRecyclerAdapter = GameGridRecyclerAdapter(gameService.getGameGrid().getCells(), this)

        grid.adapter = gameGridRecyclerAdapter

        smiley = findViewById(R.id.activity_game_smiley)
        smiley.setOnClickListener {
            gameService = GameService(GRID_SIZE, BOMB_COUNT)
            gameGridRecyclerAdapter.setCells(gameService.getGameGrid().getCells())
            timerStarted = false
            countDownTimer.cancel()
            secondsElapsed = 0
            timer.setText(R.string.default_count)

            flagsLeft.setText(
                String.format("%03d", gameService.getNumberBombs() - gameService.getFlagCount())
            )
        }

        flag = findViewById(R.id.activity_game_flag)
        flag.setOnClickListener {
            gameService.switchMode()
            val border = GradientDrawable().apply {
                setColor(0xFFFFFFFF.toInt())
            }
            if (gameService.isFlagMode()){
                border.setStroke(1, 0xFF000000.toInt())
            }
            flag.background = border
        }

    }

    override fun cellClick(cell: Cell) {
        gameService.handleCellClick(cell)
        flagsLeft.text = String.format("%03d", gameService.getNumberBombs() - gameService.getFlagCount())
        if(!timerStarted){
            countDownTimer.start()
            timerStarted = true
        }
        if(gameService.isGameOver()){
            countDownTimer.cancel()
            Toast.makeText(applicationContext, "Game Over 💀", Toast.LENGTH_SHORT).show()
            gameService.getGameGrid().revealAllBombs()
        }

        if(gameService.isGameWon()){
            countDownTimer.cancel()
            Toast.makeText(applicationContext, "You Won 🥳", Toast.LENGTH_SHORT).show()
            gameService.getGameGrid().revealAllBombs()
        }
        gameGridRecyclerAdapter.setCells(gameService.getGameGrid().getCells())
    }
}