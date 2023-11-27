/*
 * Copyright 2023 Dylan Roussel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.evowizz.cosmose.demos.pingpong

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.evowizz.cosmose.demos.pingpong.elements.Ball
import dev.evowizz.cosmose.demos.pingpong.elements.Paddle
import dev.evowizz.cosmose.demos.pingpong.elements.Wall

@Composable
fun rememberGameState(
    wallWidth: Float,
    wallRadius: Float,
    ballSize: Float,
    paddleWidth: Float,
    paddleHeight: Float,
    paddlePadding: Float
): GameState = remember(
    wallWidth,
    wallRadius,
    ballSize,
    paddleWidth,
    paddleHeight,
    paddlePadding
) {
    GameState(
        wallWidth = wallWidth,
        wallRadius = wallRadius,
        ballSize = ballSize,
        paddleWidth = paddleWidth,
        paddleHeight = paddleHeight,
        paddlePadding = paddlePadding
    )
}

class GameState(
    val wallWidth: Float,
    val wallRadius: Float,
    val ballSize: Float,
    val paddleWidth: Float,
    val paddleHeight: Float,
    val paddlePadding: Float
) {
    var score: Int by mutableStateOf(0)

    var position: Float by mutableStateOf(0.5f)

    var boardSize by mutableStateOf(Size(0f, 0f))

    val wall = Wall(width = wallWidth, radius = wallRadius)
    val ball = Ball(radius = ballSize / 2)
    val topPaddle = Paddle.Top(
        height = paddleHeight,
        width = paddleWidth,
        padding = paddlePadding,
        radius = paddleHeight / 2
    )

    val bottomPaddle = Paddle.Bottom(
        height = paddleHeight,
        width = paddleWidth,
        padding = paddlePadding,
        radius = paddleHeight / 2
    )

    fun updateBoardSize(width: Float, height: Float) {
        this.boardSize = Size(width, height)

        wall.updateBoardSize(boardSize)
        ball.updateBoardSize(boardSize)
        topPaddle.updateBoardSize(boardSize)
        bottomPaddle.updateBoardSize(boardSize)
    }

    fun updatePlayerPosition(move: Float) {
        this.position = move / boardSize.width
        topPaddle.updatePosition(1 - position)
        bottomPaddle.updatePosition(position)
    }

    fun updateBallPosition() {
        ball.updatePosition()
    }

    fun listenForCollision() {
        var paddleCollision = false

        if (topPaddle.detectCollision(ball)) {
            val ballCenter = ball.getPositionOnBoard()
            val paddleCenter = topPaddle.getPositionOnBoard()
            val distance = ballCenter.x - paddleCenter.x
            val maxDistance = (topPaddle.width / 2) + ball.radius
            val angle = (distance / maxDistance) * 45f
            // Update ball velocity based on angle
            ball.updateVelocity(
                ball.velocity.copy(
                    x = angle / 10f,
                    y = -ball.velocity.y
                )
            )

            paddleCollision = true
        }

        if (bottomPaddle.detectCollision(ball)) {
            val ballCenter = ball.getPositionOnBoard()
            val paddleCenter = bottomPaddle.getPositionOnBoard()
            val distance = ballCenter.x - paddleCenter.x
            val maxDistance = (bottomPaddle.width / 2) + ball.radius
            val angle = (distance / maxDistance) * 45f
            // Update ball velocity based on angle
            ball.updateVelocity(
                ball.velocity.copy(
                    x = angle / 10f,
                    y = -ball.velocity.y
                )
            )

            paddleCollision = true
        }

        if (wall.detectCollision(ball)) {
            ball.updateVelocity(ball.velocity.copy(x = -ball.velocity.x))
        }

        if (paddleCollision) {
            ball.handleCollision()
            score += 1
        }
    }

    fun DrawScope.drawGame(
        debug: Boolean = false
    ) {
        updateBoardSize(
            width = size.width - wallWidth * 2,
            height = size.height - wallWidth * 2
        )

        listenForCollision()
        updateBallPosition()

        with(wall) { draw(debug = debug) }
        with(ball) { draw(debug = debug) }
        with(topPaddle) { draw(debug = debug) }
        with(bottomPaddle) { draw(debug = debug) }
    }
}













