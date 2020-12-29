package com.breakingbad.data.paging

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeys(
    @PrimaryKey
    val characterId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
