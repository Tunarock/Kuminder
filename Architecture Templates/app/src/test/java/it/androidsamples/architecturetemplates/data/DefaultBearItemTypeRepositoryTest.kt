/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.androidsamples.architecturetemplates.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import it.androidsamples.architecturetemplates.data.local.database.BearItemType
import it.androidsamples.architecturetemplates.data.local.database.BearItemTypeDao

/**
 * Unit tests for [DefaultBearItemTypeRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultBearItemTypeRepositoryTest {

    @Test
    fun bearItemTypes_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultBearItemTypeRepository(FakeBearItemTypeDao())

        repository.add("Repository")

        assertEquals(repository.bearItemTypes.first().size, 1)
    }

}

private class FakeBearItemTypeDao : BearItemTypeDao {

    private val data = mutableListOf<BearItemType>()

    override fun getBearItemTypes(): Flow<List<BearItemType>> = flow {
        emit(data)
    }

    override suspend fun insertBearItemType(item: BearItemType) {
        data.add(0, item)
    }
}
