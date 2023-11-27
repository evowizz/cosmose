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

package dev.evowizz.cosmose.model

import androidx.compose.runtime.Composable
import dev.evowizz.cosmose.demos.pingpong.PingPongDemo
import dev.evowizz.cosmose.demos.template.TemplateDemo

data class Demo(
    val id: Int,
    val name: String,
    val content: @Composable () -> Unit,
)

private var nextId: Int = 1
private fun nextId(): Int = nextId.also { nextId += 1 }

private val PingPong = Demo(
    id = nextId(),
    name = "Ping Pong",
    content = { PingPongDemo() },
)

private val Template = Demo(
    id = nextId(),
    name = "Template",
    content = { TemplateDemo() },
)

val Demos = listOf(
    PingPong,
    Template,
)