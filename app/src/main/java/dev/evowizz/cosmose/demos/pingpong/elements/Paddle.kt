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

package dev.evowizz.cosmose.demos.pingpong.elements

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import dev.evowizz.cosmose.demos.pingpong.drawCenterIndicator
import kotlin.math.sqrt

sealed class Paddle(
    open val width: Float,
    open val height: Float,
    open val padding: Float,
    open val radius: Float,
) {
    internal var boardSize = Size(0f, 0f)

    internal var position by mutableStateOf(0.5f)

    fun checkBoardSize() = check(boardSize != Size.Zero) { "Board size is not set" }

    fun updateBoardSize(size: Size) {
        this.boardSize = size
    }

    fun updatePosition(position: Float) {
        this.position = position
    }

    abstract fun getPositionOnBoard(): Offset

    abstract fun detectCollision(ball: Ball): Boolean

    abstract fun DrawScope.draw(debug: Boolean = false)

    data class Top(
        override val width: Float,
        override val height: Float,
        override val padding: Float,
        override val radius: Float,
    ) : Paddle(
        width = width,
        height = height,
        padding = padding,
        radius = radius,
    ) {
        override fun getPositionOnBoard(): Offset = Offset(
            x = position * boardSize.width,
            y = 0f + padding + height / 2
        )

        override fun detectCollision(ball: Ball): Boolean {
            checkBoardSize()

            val ballRadius = ball.radius
            val ballCenter = ball.getPositionOnBoard()

            val paddleWidth = width
            val paddleHeight = height
            val paddleCenter = Offset(
                x = position * boardSize.width,
                y = 0f + padding + paddleHeight / 2
            )

            val paddleLeft = paddleCenter.x - paddleWidth / 2
            val paddleRight = paddleCenter.x + paddleWidth / 2
            val paddleTop = paddleCenter.y - paddleHeight / 2
            val paddleBottom = paddleCenter.y + paddleHeight / 2

            val collisionX = ballCenter.x.coerceIn(paddleLeft, paddleRight)
            val collisionY = ballCenter.y.coerceIn(paddleTop, paddleBottom)

            val distanceX = ballCenter.x - collisionX
            val distanceY = ballCenter.y - collisionY

            val distance = sqrt((distanceX * distanceX + distanceY * distanceY).toDouble())

            return distance < ballRadius
        }

        override fun DrawScope.draw(debug: Boolean) {
            val x = (position * size.width - width / 2)
                .coerceIn(padding, size.width - width - padding)

            val y = 0f + padding

            drawRoundRect(
                color = Color.White,
                topLeft = Offset(x, y),
                size = Size(width, height),
                cornerRadius = CornerRadius(height / 2)
            )

            if (debug) {
                val centerY = 0f + padding + height / 2

                drawCenterIndicator(
                    color = Color.Red,
                    position = Offset(x + width / 2, centerY),
                    size = 20f
                )

                drawRect(
                    color = Color.Red,
                    topLeft = Offset(x, y),
                    size = Size(width, height),
                    style = Stroke(Stroke.HairlineWidth)
                )
            }
        }
    }

    data class Bottom(
        override val width: Float,
        override val height: Float,
        override val padding: Float,
        override val radius: Float,
    ) : Paddle(
        width = width,
        height = height,
        padding = padding,
        radius = radius,
    ) {
        override fun getPositionOnBoard(): Offset = Offset(
            x = position * boardSize.width,
            y = boardSize.height - padding - height / 2
        )

        override fun detectCollision(ball: Ball): Boolean {
            val ballRadius = ball.radius
            val ballCenter = ball.getPositionOnBoard()

            val paddleWidth = width
            val paddleHeight = height
            val paddleCenter = Offset(
                x = position * boardSize.width,
                y = boardSize.height - padding - paddleHeight / 2
            )

            val paddleLeft = paddleCenter.x - paddleWidth / 2
            val paddleRight = paddleCenter.x + paddleWidth / 2
            val paddleTop = paddleCenter.y - paddleHeight / 2
            val paddleBottom = paddleCenter.y + paddleHeight / 2

            val collisionX = ballCenter.x.coerceIn(paddleLeft, paddleRight)
            val collisionY = ballCenter.y.coerceIn(paddleTop, paddleBottom)

            val distanceX = ballCenter.x - collisionX
            val distanceY = ballCenter.y - collisionY

            val distance = sqrt((distanceX * distanceX + distanceY * distanceY).toDouble())

            return distance < ballRadius
        }

        override fun DrawScope.draw(debug: Boolean) {
            val x = (position * size.width - width / 2)
                .coerceIn(padding, size.width - width - padding)

            val y = size.height - height - padding

            drawRoundRect(
                color = Color.White,
                topLeft = Offset(x, y),
                size = Size(width, height),
                cornerRadius = CornerRadius(height / 2)
            )

            if (debug) {
                val centerY = size.height - padding - height / 2

                drawCenterIndicator(
                    color = Color.Red,
                    position = Offset(x + width / 2, centerY),
                    size = 20f
                )

                drawRect(
                    color = Color.Red,
                    topLeft = Offset(x, y),
                    size = Size(width, height),
                    style = Stroke(Stroke.HairlineWidth)
                )
            }
        }
    }
}
