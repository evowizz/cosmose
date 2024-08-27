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

package dev.evowizz.cosmose.demos.hideandsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp

@Composable
fun MagicSheet(
    isOpen: Boolean,
    sheetState: SheetState = rememberModalBottomSheetState(true),
    contentOpenWindowInsets: WindowInsets = MagicSheetDefaults.ContentOpenInsets,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
    sheetContent: @Composable () -> Unit
) {
    val sheetProgress by sheetState.progressAsState()

    val scale by derivedStateOf {
        lerp(
            start = MagicSheetDefaults.ContentDefaultScale,
            stop = MagicSheetDefaults.ContentLowestScale,
            fraction = sheetProgress
        )
    }
    val roundedCorners by derivedStateOf {
        lerp(
            start = MagicSheetDefaults.ContentMinRoundedCorners,
            stop = MagicSheetDefaults.ContentMaxRoundedCorners,
            fraction = sheetProgress
        )
    }

    val density = LocalDensity.current
    val topPadding by remember(contentOpenWindowInsets) {
        derivedStateOf { with(density) { contentOpenWindowInsets.getTop(this).toDp() } }
    }

    val contentTopPadding by derivedStateOf {
        lerp(
            start = MagicSheetDefaults.ContentDefaultTopPadding,
            stop = topPadding,
            fraction = sheetProgress
        )
    }

    val scrimAlpha by derivedStateOf {
        lerp(
            start = MagicSheetDefaults.ScrimDefaultAlpha,
            stop = MagicSheetDefaults.ScrimMaxAlpha,
            fraction = sheetProgress
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .scale(scale)
                .padding(top = contentTopPadding),
            shape = RoundedCornerShape(roundedCorners),
            content = content
        )

        if (isOpen) {
            // Custom scrim, based on the sheet progress.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MagicSheetDefaults.ScrimColor.copy(alpha = scrimAlpha))
            )

            ModalBottomSheet(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(top = MagicSheetDefaults.SheetAdditionalPadding),
                onDismissRequest = onDismiss,
                scrimColor = Color.Transparent,
                sheetState = sheetState,
                content = { sheetContent() }
            )
        }
    }
}

object MagicSheetDefaults {

    val SheetAdditionalPadding = 32.dp

    val ContentDefaultScale = 1f
    val ContentLowestScale = 0.96f

    val ContentMinRoundedCorners = 0.dp
    val ContentMaxRoundedCorners = 16.dp

    val ContentDefaultTopPadding = 0.dp
    val ContentOpenInsets: WindowInsets
        @Composable
        get() = WindowInsets.statusBars.only(WindowInsetsSides.Top)

    val ScrimDefaultAlpha = 0f
    val ScrimMaxAlpha = 0.32f
    val ScrimColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.scrim
}

// Thank you, Sasikanth!
// See: https://x.com/its_sasikanth/status/1827614107412898145
@Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
private fun SheetState.progressAsState(): State<Float> {
    // The reason we are creating our own progress is because
    // anchoredDraggableState.progress is not working as expected.
    return derivedStateOf {
        val hiddenAnchor = anchoredDraggableState.anchors.positionOf(SheetValue.Hidden)
        val offset = anchoredDraggableState.offset
        when {
            hiddenAnchor.isNaN() -> 0f
            else -> 1 - (offset / hiddenAnchor)
        }
    }
}