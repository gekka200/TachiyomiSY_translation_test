package eu.kanade.presentation.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import eu.kanade.presentation.components.TextButton
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.database.models.LibraryManga
import eu.kanade.tachiyomi.ui.library.LibraryItem

@Composable
fun LibraryCompactGrid(
    items: List<LibraryItem>,
    columns: Int,
    selection: List<LibraryManga>,
    onClick: (LibraryManga) -> Unit,
    onLongClick: (LibraryManga) -> Unit,
    searchQuery: String?,
    onGlobalSearchClicked: () -> Unit,
    // SY -->
    onOpenReader: (LibraryManga) -> Unit,
    // SY <--
) {
    LazyLibraryGrid(
        columns = columns,
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            if (searchQuery.isNullOrEmpty().not()) {
                TextButton(onClick = onGlobalSearchClicked) {
                    Text(
                        text = stringResource(R.string.action_global_search_query, searchQuery!!),
                        modifier = Modifier.zIndex(99f),
                    )
                }
            }
        }

        items(
            items = items,
            key = {
                it.manga.id!!
            },
        ) { libraryItem ->
            LibraryCompactGridItem(
                item = libraryItem,
                isSelected = libraryItem.manga in selection,
                onClick = onClick,
                onLongClick = onLongClick,
                // SY -->
                onOpenReader = onOpenReader,
                // SY <--
            )
        }
    }
}

@Composable
fun LibraryCompactGridItem(
    item: LibraryItem,
    isSelected: Boolean,
    onClick: (LibraryManga) -> Unit,
    onLongClick: (LibraryManga) -> Unit,
    // SY -->
    onOpenReader: (LibraryManga) -> Unit,
    // SY <--
) {
    val manga = item.manga
    LibraryGridCover(
        modifier = Modifier
            .selectedOutline(isSelected)
            .combinedClickable(
                onClick = {
                    onClick(manga)
                },
                onLongClick = {
                    onLongClick(manga)
                },
            ),
        mangaCover = eu.kanade.domain.manga.model.MangaCover(
            manga.id!!,
            manga.source,
            manga.favorite,
            manga.thumbnail_url,
            manga.cover_last_modified,
        ),
        downloadCount = item.downloadCount,
        unreadCount = item.unreadCount,
        isLocal = item.isLocal,
        language = item.sourceLanguage,
        // SY -->
        showPlayButton = item.startReadingButton,
        playButtonPosition = PlayButtonPosition.Top,
        onOpenReader = {
            onOpenReader(manga)
        },
        // SY <--
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        1f to Color(0xAA000000),
                    ),
                )
                .fillMaxHeight(0.33f)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        )
        Text(
            text = manga.title,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomStart),
            maxLines = 2,
            style = LocalTextStyle.current.copy(
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                shadow = Shadow(
                    color = Color.Black,
                    blurRadius = 4f,
                ),
            ),
        )
    }
}