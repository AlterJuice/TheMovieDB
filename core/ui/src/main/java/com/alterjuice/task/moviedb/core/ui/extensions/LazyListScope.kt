package com.alterjuice.task.moviedb.core.ui.extensions

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems

fun <T: Any> LazyListScope.pagedItems(
    items: LazyPagingItems<T>,
    key: ((index: Int) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(T) -> Unit
) = items(
    count = items.itemCount,
    key = key,
    itemContent = { index -> items[index]?.let { itemContent(it) } }
)