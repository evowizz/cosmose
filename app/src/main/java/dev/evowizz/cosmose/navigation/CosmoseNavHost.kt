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

package dev.evowizz.cosmose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.evowizz.cosmose.model.Demo
import dev.evowizz.cosmose.model.Demos
import dev.evowizz.cosmose.ui.demo.DemoIdArgName
import dev.evowizz.cosmose.ui.demo.DemoRoute
import dev.evowizz.cosmose.ui.demo.DemoScreen
import dev.evowizz.cosmose.ui.home.HomeRoute
import dev.evowizz.cosmose.ui.home.HomeScreen

@Composable
fun CosmoseNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = HomeRoute,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(HomeRoute) {
            HomeScreen(
                demos = Demos,
                onDemoClick = { demo -> navController.navigate(demo.route()) }
            )
        }

        composable(
            route = "$DemoRoute/" +
            "{$DemoIdArgName}",
            arguments = listOf(
                navArgument(DemoIdArgName) { type = NavType.IntType }
            )
        ) { navBackStackEntry ->
            val arguments = requireNotNull(navBackStackEntry.arguments) { "No arguments" }
            val demoId = arguments.getInt(DemoIdArgName)
            val demo = Demos.first { it.id == demoId }

            DemoScreen(demo = demo)
        }
    }
}

private fun Demo.route(): String = "$DemoRoute/$id"