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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.evowizz.cosmose.demos.pingpong.drawCenterIndicator

data class Ball(
    val radius: Float,
    val maxSpeed: Float = 6f,
) {
    private var boardSize = Size(0f, 0f)
    private var position by mutableStateOf(Offset(0.5f, 0.5f))

    var speed by mutableStateOf(1f)
    var velocity by mutableStateOf(Offset(0.1f, 5f))
        private set

    fun updateBoardSize(size: Size) {
        this.boardSize = size
    }

    fun getPositionOnBoard(): Offset = Offset(
        x = position.x * boardSize.width,
        y = position.y * boardSize.height
    )

    fun updateVelocity(velocity: Offset) {
        this.velocity = velocity
    }

    fun updatePosition() {
        val fixedVelocity = Offset(
            x = velocity.x / boardSize.width,
            y = velocity.y / boardSize.height
        )

        this.position += fixedVelocity * speed
    }

    fun handleCollision() {
        if (speed < maxSpeed) {
            speed += 0.1f
        }
    }

    fun DrawScope.draw(debug: Boolean = false) {
        val ballPosition = Offset(position.x * size.width, position.y * size.height)

        drawCircle(
            color = Color.White,
            center = ballPosition,
            radius = radius,
        )

        if (debug) {
            drawCenterIndicator(
                color = Color.Red,
                position = ballPosition,
                size = 20f
            )
        }
    }
}