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

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

fun DrawScope.drawCenterIndicator(
    position: Offset,
    size: Float,
    color: Color = Color.Red,
) {
    drawLine(
        color = color,
        start = Offset(position.x - size / 2, position.y),
        end = Offset(position.x + size / 2, position.y),
        strokeWidth = 2f
    )
    drawLine(
        color = color,
        start = Offset(position.x, position.y - size / 2),
        end = Offset(position.x, position.y + size / 2),
        strokeWidth = 2f
    )
}