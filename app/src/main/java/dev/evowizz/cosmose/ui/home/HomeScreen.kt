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

package dev.evowizz.cosmose.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.evowizz.cosmose.R
import dev.evowizz.cosmose.model.Demo
import dev.evowizz.cosmose.ui.demo.DemoItem

val HomeRoute = "home"

/**
 * The layout of the home screen is heavily inspired by the Material 3 Catalog app.
 *
 * See: https://android.googlesource.com/platform/frameworks/support/+/b023c33ba6be0d81d797e31a1a87289fbff5c849/compose/material3/material3/integration-tests/material3-catalog/src/main/java/androidx/compose/material3/catalog/library/ui/home/Home.kt
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    demos: List<Demo>,
    onDemoClick: (Demo) -> Unit,
) {
    val ltr = LocalLayoutDirection.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = Modifier.consumeWindowInsets(paddingValues),
            verticalArrangement = Arrangement.spacedBy(HomePadding),
            horizontalArrangement = Arrangement.spacedBy(HomePadding),
            columns = GridCells.Adaptive(HomeCellMinSize),
            contentPadding = PaddingValues(
                start = paddingValues.calculateStartPadding(ltr) + HomePadding,
                end = paddingValues.calculateEndPadding(ltr) + HomePadding,
                top = paddingValues.calculateTopPadding() + HomePadding,
                bottom = paddingValues.calculateBottomPadding() + HomePadding,
            ),
            content = {
                items(demos) { demo ->
                    DemoItem(
                        demo = demo,
                        onClick = onDemoClick
                    )
                }
            },
        )
    }
}

private val HomeCellMinSize = 180.dp
private val HomePadding = 16.dp