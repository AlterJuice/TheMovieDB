package com.alterjuice.task.moviedb.core.ui.extensions

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey

/**
 * Displays paged items in a [LazyListScope] using the provided [LazyPagingItems].
 *
 * This function simplifies rendering paged data by wrapping [LazyListScope.items] with
 * default [key] and [contentType] logic specific to [LazyPagingItems], while allowing
 * customization via the optional parameters.
 *
 * @param items The paged data source to render
 * @param key Optional function to define stable keys per item. Defaults to the Paging library's
 *            default key generation
 * @param contentType Optional function to specify content types for items. Defaults to null
 * @param itemContent Composable builder for each item. Receives the item as receiver with the
 *                    current index - only triggered when the item is non-null
 */
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