/*
 * Copyright 2024 Dylan Roussel
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

package dev.evowizz.cosmose.demos.heyhey.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.evowizz.cosmose.ui.theme.CosmoseTheme

@Composable
fun Composer(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {
    // CircleShape is a RoundedCornerShape with 50% corner size
    // So no, our Composer is not going to be a ball.
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceDim,
        modifier = modifier.height(56.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WithTextStyle(MaterialTheme.typography.bodyLarge) {
                InnerTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    hint = "Type a message",
                    value = value,
                    onValueChange = onValueChange
                )
            }

            // Someone is going to see this and think "Aw gawd this is awful"
            // and if that person is you, I'm sorry.
            // Don't do this at home, kids.
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                IconButton(onClick = { if (value.isNotBlank()) onSend() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Send,
                        contentDescription = "Send"
                    )
                }
            }
        }
    }
}

@Composable
private fun InnerTextField(
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    hint: String = "",
    value: String,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        if (value.isEmpty()) {
            Text(
                text = hint,
                style = style,
                color = LocalContentColor.current.copy(alpha = 0.56f)
            )
        }

        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            textStyle = style,
            cursorBrush = SolidColor(LocalContentColor.current)
        )
    }
}

@Composable
private fun WithTextStyle(
    style: TextStyle,
    color: Color = LocalContentColor.current,
    content: @Composable () -> Unit
) {
    val textStyle = style.copy(color = color)
    CompositionLocalProvider(
        LocalTextStyle provides textStyle,
        LocalContentColor provides color,
        content = content
    )
}

@Preview
@Composable
private fun ComposerPreview() {
    var value by remember { mutableStateOf("Hey hey") }

    CosmoseTheme {
        Composer(
            value = value,
            onValueChange = { value = it },
            onSend = { value = "" }
        )
    }
}