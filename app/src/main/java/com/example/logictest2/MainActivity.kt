package com.example.logictest2

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.logictest2.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    var playerScore = 0
    var computerScore = 0
    var round = 0
    val WIN_SCORE = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btRock.setOnClickListener { play("rock") }
        binding.btPaper.setOnClickListener { play("paper") }
        binding.btScissor.setOnClickListener { play("scissor") }

        binding.fbRestart.setOnClickListener { restartGame() }

        restartGame()
    }

    fun play(playerMove: String) = with(binding) {
        // show images area
        ivPlayer.visibility = ImageView.VISIBLE
        ivComputer.visibility = ImageView.VISIBLE

        val moves = listOf("rock", "paper", "scissor")
        val compMove = moves[Random.nextInt(3)]

        setMoveImage(ivPlayer, playerMove)
        setMoveImage(ivComputer, compMove)

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

    private fun updateScoreText() = with(binding) {
        tvPlayerScore.text = "Your Score $playerScore/$WIN_SCORE"
        tvComputerScore.text = "Computer Score $computerScore/$WIN_SCORE"
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

    private fun onGameOver(playerWon: Boolean) = with(binding) {
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
        AlertDialog.Builder(this@MainActivity)
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

    fun restartGame() = with(binding) {
        playerScore = 0
        computerScore = 0
        round = 0

        updateScoreText()
        tvResult.text = "Click a button to start!"

        ivPlayer.visibility = ImageView.INVISIBLE
        ivComputer.visibility = ImageView.INVISIBLE

        // re-enable move buttons so user can play again
        setMoveButtonsEnabled(true)
    }

    private fun setMoveButtonsEnabled(enabled: Boolean) = with(binding) {
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