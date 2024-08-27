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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.evowizz.cosmose.ui.theme.CosmoseTheme
import kotlinx.coroutines.launch

@Composable
fun HideAndSheetDemo() {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(true)
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    MagicSheet(
        isOpen = openBottomSheet,
        onDismiss = { openBottomSheet = false },
        sheetState = sheetState,
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Main Content")
                    Button(onClick = { openBottomSheet = true }) {
                        Text("Show Bottom Sheet")
                    }
                }
            }
        },
        sheetContent = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "This is the sheet content",
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(
                    onClick = {
                        scope
                            .launch { sheetState.hide() }
                            .invokeOnCompletion {
                                if (!sheetState.isVisible) openBottomSheet = false
                            }
                    }
                ) {
                    Text("Close Sheet")
                }
            }
        }
    )
}

@Preview
@Composable
private fun HideAndSheetDemoPreview() {
    CosmoseTheme {
        HideAndSheetDemo()
    }
}