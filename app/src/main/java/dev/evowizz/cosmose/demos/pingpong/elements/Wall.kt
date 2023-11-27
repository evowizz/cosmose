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

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

data class Wall(
    val width: Float,
    val radius: Float,
) {
    private var boardSize = Size(0f, 0f)

    fun checkBoardSize() = check(boardSize != Size.Zero) { "Board size is not set" }

    fun updateBoardSize(size: Size) {
        this.boardSize = size
    }

    fun detectCollision(ball: Ball): Boolean {
        checkBoardSize()

        val ballRadius = ball.radius
        val ballCenter = ball.getPositionOnBoard()

        val wallMinLeft = 0f
        val wallMinRight = boardSize.width
        val wallMinTop = 0f
        val wallMinBottom = boardSize.height

        return ballCenter.x - ballRadius <= wallMinLeft ||
            ballCenter.x + ballRadius >= wallMinRight ||
            ballCenter.y - ballRadius <= wallMinTop ||
            ballCenter.y + ballRadius >= wallMinBottom
    }

    fun DrawScope.draw(debug: Boolean = false) {
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(width / 2, width / 2),
            size = Size(size.width - width, size.height - width),
            style = Stroke(width),
            cornerRadius = CornerRadius(radius)
        )

        if (debug) {
            drawRect(color = Color.Red, style = Stroke(Stroke.HairlineWidth))
        }
    }
}
