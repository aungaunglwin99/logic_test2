package com.example.logictest2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AlertDialog
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var imPlayer: ImageView
    lateinit var imComputer: ImageView
    lateinit var tvScore: TextView
    lateinit var tvResult: TextView

    lateinit var btRock: Button
    lateinit var btPaper: Button
    lateinit var btScissor: Button
    lateinit var fBtRestart: FloatingActionButton

    var playerScore = 0
    var computerScore = 0
    var round = 0
    val WIN_SCORE = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imPlayer = findViewById(R.id.ivPlayer)
        imComputer = findViewById(R.id.ivComputer)
        tvScore = findViewById(R.id.tvScore)
        tvResult = findViewById(R.id.tvResult)

        btRock = findViewById(R.id.btRock)
        btPaper = findViewById(R.id.btPaper)
        btScissor = findViewById(R.id.btScissor)
        fBtRestart = findViewById(R.id.fBtRestart)

        btRock.setOnClickListener { play("rock") }
        btPaper.setOnClickListener { play("paper") }
        btScissor.setOnClickListener { play("scissor") }

        fBtRestart.setOnClickListener { restartGame() }

        // initial UI
        restartGame()
    }

    fun play(playerMove: String) {
        // show images area
        imPlayer.visibility = ImageView.VISIBLE
        imComputer.visibility = ImageView.VISIBLE

        val moves = listOf("rock", "paper", "scissor")
        val compMove = moves[Random.nextInt(3)]

        setMoveImage(imPlayer, playerMove)
        setMoveImage(imComputer, compMove)

        round++

        val result = checkWin(playerMove, compMove)

        when (result) {
            "win" -> {
                playerScore++
                tvResult.text = "You Win Round $round!"
            }
            "lose" -> {
                computerScore++
                tvResult.text = "You Lose Round $round!"
            }
            else -> tvResult.text = "Round $round is a Draw!"
        }

        updateScoreText()
        checkGameOverIfNeeded()
    }

    private fun updateScoreText() {
        tvScore.text = "Your Score $playerScore/$WIN_SCORE      Computer Score $computerScore/$WIN_SCORE"
    }

    private fun checkGameOverIfNeeded() {
        when {
            playerScore >= WIN_SCORE -> {
                onGameOver(playerWon = true)
            }
            computerScore >= WIN_SCORE -> {
                onGameOver(playerWon = false)
            }
            else -> {
                // game continues
            }
        }
    }

    private fun onGameOver(playerWon: Boolean) {
        // disable move buttons
        setMoveButtonsEnabled(false)

        // show final message in UI
        val message = if (playerWon) {
            "Congratulations — you won the game!\nFinal score: $playerScore : $computerScore"
        } else {
            "Computer wins the game.\nFinal score: $playerScore : $computerScore"
        }
        tvResult.text = "Game Over"

        // show AlertDialog to announce winner and show option to restart
        AlertDialog.Builder(this)
            .setTitle(if (playerWon) "You Win!" else "You Lose")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Restart") { _, _ ->
                restartGame()
            }
            .setNegativeButton("Close") { dialog, _ ->
                dialog.dismiss()
                // keep disabled until user restarts manually with FAB
            }
            .show()
    }

    fun restartGame() {
        playerScore = 0
        computerScore = 0
        round = 0

        tvScore.text = "Your Score 0/$WIN_SCORE      Computer Score 0/$WIN_SCORE"
        tvResult.text = "Click a button to start!"

        imPlayer.visibility = ImageView.INVISIBLE
        imComputer.visibility = ImageView.INVISIBLE

        // re-enable move buttons so user can play again
        setMoveButtonsEnabled(true)
    }

    private fun setMoveButtonsEnabled(enabled: Boolean) {
        btRock.isEnabled = enabled
        btPaper.isEnabled = enabled
        btScissor.isEnabled = enabled

        // optionally fade buttons visually when disabled
        val alpha = if (enabled) 1.0f else 0.5f
        btRock.alpha = alpha
        btPaper.alpha = alpha
        btScissor.alpha = alpha
    }

    fun setMoveImage(view: ImageView, move: String) {
        val img = when (move) {
            "rock" -> R.drawable.rock
            "paper" -> R.drawable.paper
            else -> R.drawable.scissor
        }
        view.setImageResource(img)
    }

    fun checkWin(player: String, comp: String): String {
        if (player == comp) return "draw"

        return when {
            player == "rock" && comp == "scissor" -> "win"
            player == "paper" && comp == "rock" -> "win"
            player == "scissor" && comp == "paper" -> "win"
            else -> "lose"
        }
    }
}