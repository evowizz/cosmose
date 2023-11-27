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

package dev.evowizz.cosmose.ui.demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.evowizz.cosmose.model.Demo
import dev.evowizz.cosmose.model.Demos
import dev.evowizz.cosmose.ui.theme.CosmoseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoItem(
    demo: Demo,
    onClick: (component: Demo) -> Unit
) {
    OutlinedCard(
        onClick = { onClick(demo) },
        modifier = Modifier.height(ComponentItemHeight)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(ComponentItemInnerPadding)
        ) {

            Icon(
                modifier = Modifier
                    .size(ComponentItemIconSize)
                    .align(Alignment.Center),
                imageVector = Icons.Rounded.Close,
                contentDescription = null
            )

            Text(
                text = demo.name,
                modifier = Modifier.align(Alignment.BottomStart),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
private fun DemoItemPreview() {
    CosmoseTheme {
        DemoItem(
            demo = Demos[0],
            onClick = {}
        )
    }
}

private val ComponentItemHeight = 220.dp
private val ComponentItemOuterPadding = 4.dp
private val ComponentItemInnerPadding = 16.dp
private val ComponentItemIconSize = 80.dp