package com.alterjuice.task.moviedb.core.ui.extensions

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey

fun <T: Any> LazyListScope.pagedItems(
    items: LazyPagingItems<T>,
    key: ((T) -> Any)? = null,
    contentType: ((T) -> Any?) = { null },
    itemContent: @Composable LazyItemScope.(T) -> Unit
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key),
        contentType = items.itemContentType(contentType),
        itemContent = { index -> items[index]?.let { itemContent(it) } }
    )
}