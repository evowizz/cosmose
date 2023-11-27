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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Ping Pong demo.
 */
@Composable
fun BoxScope.Game(
    wallWidth: Dp = 4.dp,
    wallRadius: Dp = 28.dp,
    ballSize: Dp = 32.dp,
    paddleWidth: Dp = 128.dp,
    paddleHeight: Dp = 32.dp,
    paddlePadding: Dp = 16.dp,
    state: GameState = rememberGameState(
        wallWidth = with(LocalDensity.current) { wallWidth.toPx() },
        wallRadius = with(LocalDensity.current) { wallRadius.toPx() },
        ballSize = with(LocalDensity.current) { ballSize.toPx() },
        paddleWidth = with(LocalDensity.current) { paddleWidth.toPx() },
        paddleHeight = with(LocalDensity.current) { paddleHeight.toPx() },
        paddlePadding = with(LocalDensity.current) { paddlePadding.toPx() },
    ),
    debug: Boolean = false,
) {
    Canvas(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    when (event.type) {
                        PointerEventType.Move -> {
                            val x = event.changes.first().position.x
                            state.updatePlayerPosition(x)
                        }
                    }
                }
            }
        }
    ) {
        with(state) { drawGame(debug) }
    }

    Text(
        modifier = Modifier.align(Alignment.Center),
        text = state.score.toString(),
        color = Color.White.copy(alpha = 0.5f),
        fontSize = 48.sp
    )

    if (debug) {
        val text = "Board size: ${state.boardSize}" +
            "\nBall position: ${state.ball.getPositionOnBoard()}" +
            "\nBall velocity: ${state.ball.velocity}" +
            "\nBall speed: ${state.ball.speed}" +
            "\nTop paddle position: ${state.topPaddle.getPositionOnBoard()}" +
            "\nBottom paddle position: ${state.bottomPaddle.getPositionOnBoard()}" +
            "\nPlayer position: ${state.position}" +
            "\nScore: ${state.score}"

        Text(
            modifier = Modifier.padding(
                vertical = wallWidth + paddleHeight + paddlePadding + 16.dp,
                horizontal = wallWidth + 16.dp,
            ),
            text = text,
            color = Color.White
        )
    }
}

@Preview
@Composable
private fun PingPongGamePreview() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        Game()
    }
}