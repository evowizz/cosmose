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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.ResizeMode
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.evowizz.cosmose.demos.heyhey.data.InitialMessages
import dev.evowizz.cosmose.demos.heyhey.model.MessageEntity
import dev.evowizz.cosmose.ui.theme.CosmoseTheme

@Composable
fun MessageList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    messages: List<MessageEntity>,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    with(sharedTransitionScope) {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(bottom = 16.dp),
            state = state
        ) {
            itemsIndexed(messages) { index, message ->
                val isSelf = message.isSelf
                val nextIsSelf = index != messages.size - 1 && messages[index + 1].isSelf == isSelf
                val previousIsSelf = index != 0 && messages[index - 1].isSelf == isSelf

                // True if the current message is the first of a group of messages from the same sender
                // or is the first message in the whole list
                val isAtTop = index == 0 || (!previousIsSelf || nextIsSelf)
                // isAtBottom means that the current message is the last of a group of messages from the same sender
                // unless it is the last message in the whole list
                val isAtBottom = index != 0 && previousIsSelf

                val spacerHeight = if (nextIsSelf) 2.dp else 8.dp
                Box(
                    modifier = Modifier.fillParentMaxWidth(),
                    contentAlignment = if (isSelf) Alignment.TopEnd else Alignment.TopStart
                ) {
                    MessageBubble(
                        modifier = Modifier
                            .padding(bottom = if (index == messages.size - 1) 0.dp else spacerHeight)
                            .widthIn(max = 280.dp)
                            .sharedBounds(
                                boundsTransform = { _, _ -> spring(stiffness = StiffnessLow) },
                                sharedContentState = rememberSharedContentState(index),
                                animatedVisibilityScope = animatedVisibilityScope,
                                resizeMode = ResizeMode.RemeasureToBounds
                            ),
                        isSelf = isSelf,
                        isAtTop = isAtTop,
                        isAtBottom = isAtBottom,
                        content = message.content
                    )
                }
            }
        }
    }
}

@Composable
fun SharedTransitionScope.MessageBubble(
    modifier: Modifier = Modifier,
    isSelf: Boolean,
    isAtTop: Boolean,
    isAtBottom: Boolean,
    content: String
) {
    Surface(
        modifier = modifier,
        shape = MessageBubbleDefaults.getBubbleShape(isSelf, isAtTop, isAtBottom),
        color = MessageBubbleDefaults.getBubbleColor(isSelf),
    ) {
        Box {
            Text(
                modifier = Modifier
                    .skipToLookaheadSize()
                    .align(Alignment.CenterStart)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                text = content,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

object MessageBubbleDefaults {

    val DefaultCornerSize = CornerSize(20.dp)
    val SmallCornerSize = CornerSize(4.dp)

    @Composable
    fun getBubbleColor(isSelf: Boolean): Color =
        if (isSelf) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceContainerHigh

    @Composable
    fun getBubbleShape(
        isSelf: Boolean,
        isAtTop: Boolean,
        isAtBottom: Boolean,
    ): CornerBasedShape = RoundedCornerShape(
        topStart = if (!isSelf && isAtBottom) SmallCornerSize else DefaultCornerSize,
        bottomStart = if (!isSelf && isAtTop) SmallCornerSize else DefaultCornerSize,
        topEnd = if (isSelf && isAtBottom) SmallCornerSize else DefaultCornerSize,
        bottomEnd = if (isSelf && isAtTop) SmallCornerSize else DefaultCornerSize,
    )
}

@Preview(showBackground = false)
@Composable
private fun MessageBubblePreview() {
    CosmoseTheme {
        SharedTransitionLayout {
            AnimatedVisibility(true) {
                MessageList(
                    messages = InitialMessages,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedVisibility
                )
            }
        }
    }
}
