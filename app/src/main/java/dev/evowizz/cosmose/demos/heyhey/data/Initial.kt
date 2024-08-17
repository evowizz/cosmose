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

package dev.evowizz.cosmose.demos.heyhey.data

import dev.evowizz.cosmose.demos.heyhey.model.MessageEntity
import dev.evowizz.cosmose.utils.Random
import java.util.UUID

val InitialMessages = listOf(
    createInitialMessage(
        isSelf = false,
        content = "A cat has claws at the end of paws; A comma is a pause at the end of a clause.",
    ),
    createInitialMessage(
        isSelf = true,
        content = "what",
    ),
    createInitialMessage(
        isSelf = true,
        content = "I don't know",
    ),
    createInitialMessage(
        isSelf = false,
        content = "What's the difference between a cat and a comma?",
    ),
    createInitialMessage(
        isSelf = true,
        content = "Hey Hey!",
    ),
    createInitialMessage(
        isSelf = false,
        content = "Hey there!",
    ),
)


private fun createInitialMessage(
    isSelf: Boolean,
    content: String,
) = MessageEntity(
    id = Random.uuid(),
    isSelf = isSelf,
    content = content
)