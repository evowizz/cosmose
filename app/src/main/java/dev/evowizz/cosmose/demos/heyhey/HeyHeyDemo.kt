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

package dev.evowizz.cosmose.demos.heyhey

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.PlaceHolderSize
import androidx.compose.animation.SharedTransitionScope.ResizeMode
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.evowizz.cosmose.demos.heyhey.components.Composer
import dev.evowizz.cosmose.demos.heyhey.components.MessageList
import dev.evowizz.cosmose.demos.heyhey.data.InitialMessages
import dev.evowizz.cosmose.demos.heyhey.model.MessageEntity
import dev.evowizz.cosmose.ui.theme.CosmoseTheme
import dev.evowizz.cosmose.utils.Random

@Composable
fun HeyHeyDemo() {
    val messages = remember { InitialMessages.toMutableStateList() }
    var isSending by remember { mutableStateOf(false) }
    var value by remember { mutableStateOf("") }
    var currentId by remember { mutableStateOf(Random.uuid()) }

    SharedTransitionLayout {
        AnimatedContent(
            targetState = isSending,
            label = "isSending",
            transitionSpec = HeyHeyDefaults.ComposerTransitionSpec
        ) { sending ->
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                topBar = {
                    TopAppBar(
                        modifier = Modifier
                            .renderInSharedTransitionScopeOverlay(zIndexInOverlay = 10f)
                            .padding(horizontal = 8.dp),
                        title = { Text("Jane Doe") },
                        // We cannot use Color.Transparent, since the TopAppBar is visible during
                        // the transition, which causes the messages to be visible during the transition
                        // So we use the same color as the Scaffold's containerColor.
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        ),
                        navigationIcon = {
                            FilledIconButton(
                                onClick = { /* no-op */ },
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.background
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                },
                bottomBar = {
                    ComposerContainer(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                        id = currentId,
                        value = value,
                        onValueChange = { value = it },
                        isSending = sending,
                        onSend = {
                            isSending = true
                            messages.add(
                                index = 0,
                                element = MessageEntity(
                                    id = currentId,
                                    isSelf = true,
                                    content = value,
                                )
                            )
                        }
                    )
                }
            ) {
                val listState: LazyListState = rememberLazyListState()

                LaunchedEffect(messages.size) {
                    // When the size of messageList changes, it means we have a new message.
                    // So, we reset isSending and value, we generate a new id
                    // and we scroll to the last item.
                    isSending = false
                    currentId = Random.uuid()
                    value = ""
                    listState.scrollToItem(0)
                }

                Surface(
                    modifier = Modifier.padding(it),
                    color = MaterialTheme.colorScheme.background,
                    shape = HeyHeyDefaults.ScreenContainerShape
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        MessageList(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            state = listState,
                            messages = messages,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@AnimatedContent
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ComposerContainer(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    id: String,
    value: String = "",
    onValueChange: (String) -> Unit,
    isSending: Boolean,
    onSend: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = HeyHeyDefaults.ComposerContainerShape,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Row(
            modifier = Modifier
                .windowInsetsPadding(HeyHeyDefaults.ComposerContainerWindowInsets)
                .imePadding()
                .height(HeyHeyDefaults.ComposerContainerHeight),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(
                visible = value.isEmpty() && !isSending,
                label = "Add button",
            ) {
                // Remember folks, don't do this at home.
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    IconButton(
                        modifier = Modifier.padding(start = HeyHeyDefaults.ComposerHorizontalPadding),
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AddCircleOutline,
                            contentDescription = null
                        )
                    }
                }
            }

            Box(modifier = Modifier.padding(horizontal = HeyHeyDefaults.ComposerHorizontalPadding)) {
                with(sharedTransitionScope) {
                    Composer(
                        Modifier.sharedBounds(
                            resizeMode = ResizeMode.RemeasureToBounds,
                            placeHolderSize = PlaceHolderSize.animatedSize,
                            sharedContentState = rememberSharedContentState(id),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                        value = value,
                        onValueChange = onValueChange,
                        onSend = onSend
                    )
                }
            }
        }
    }
}

object HeyHeyDefaults {
    val ScreenContainerShape: Shape
        @Composable
        get() = MaterialTheme.shapes.extraLarge.copy(
            bottomStart = ZeroCornerSize,
            bottomEnd = ZeroCornerSize
        )

    val ComposerContainerHeight = 88.dp

    val ComposerContainerShape: Shape
        @Composable
        get() = MaterialTheme.shapes.medium.copy(
            bottomStart = ZeroCornerSize,
            bottomEnd = ZeroCornerSize
        )

    val ComposerContainerWindowInsets: WindowInsets
        @Composable
        get() = BottomAppBarDefaults.windowInsets

    val ComposerHorizontalPadding = 16.dp

    val ComposerTransitionSpec: AnimatedContentTransitionScope<Boolean>.() -> ContentTransform =
        {
            (fadeIn(animationSpec = tween(220, delayMillis = 0)))
                .togetherWith(fadeOut(animationSpec = tween(90)))
        }
}

@Preview(showBackground = true)
@Composable
private fun HeyHeyDemoPreview() {
    CosmoseTheme {
        HeyHeyDemo()
    }
}