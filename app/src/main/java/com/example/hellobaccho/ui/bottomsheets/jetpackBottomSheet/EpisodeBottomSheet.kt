import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.ExperimentalComposeUiApi

private fun getEpisodeTitle(url: String): String {
    val episodeNumber = url.substringAfterLast("/")
    return "Episode $episodeNumber"
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun EpisodeBottomSheet(
    title: String,
    episodeUrls: List<String>?,
    onCardClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    // Map URLs to Episode Titles
    val episodes = episodeUrls?.map { url -> getEpisodeTitle(url) } ?: emptyList()

    // Filter the episodes based on the search text
    val filteredEpisodes = episodes.filter { it.contains(searchText, ignoreCase = true) }

    val keyboardController = LocalSoftwareKeyboardController.current

    ModalBottomSheetLayout(
        sheetContent = {
            Column(modifier = Modifier.fillMaxSize()) {
                TopAppBar(
                    title = { Text(text = title) },
                    actions = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Filled.Close, contentDescription = "Close")
                        }
                    },
                    backgroundColor = Color.Blue,
                    contentColor = Color.White
                )
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search Here...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )
                if (filteredEpisodes.isEmpty()) {
                    Text(
                        text = "No episodes found",
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn {
                        items(filteredEpisodes) { episode ->
                            EpisodeListItem(episode = episode, onClick = { onCardClick(episode) })
                        }
                    }
                }
            }
        },
        sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)
    ) {

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EpisodeListItem(episode: String, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        text = { Text(text = episode) }
    )
}

@Preview
@Composable
fun PreviewEpisodeBottomSheet() {
    EpisodeBottomSheet(
        title = "Episodes",
        episodeUrls = listOf(
            "https://api.example.com/episodes/1",
            "https://api.example.com/episodes/2",
            "https://api.example.com/episodes/3"
        ),
        onCardClick = {},
        onDismiss = {}
    )
}
