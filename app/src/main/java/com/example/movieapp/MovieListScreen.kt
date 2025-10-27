package com.example.movieapp


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movieapp.viewModel.MovieListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MovieListScreen(
    onMovieClick: (String) -> Unit,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movies") },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(MovieListEvent.ToggleSortDialog) }) {
                        Icon(painterResource(R.drawable.filter), contentDescription = "Sort")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Search Bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { query ->
                    viewModel.onEvent(MovieListEvent.OnSearchQueryChange(query))
                },
                placeholder = { Text("Search movies...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Movie List
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error: ${state.error}")
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(state.movies) { movie ->
                            MovieListItem(
                                movie = movie,
                                onItemClick = { onMovieClick(movie.id) },
                                onBookmarkClick = {
                                    viewModel.onEvent(MovieListEvent.ToggleBookmark(movie.id))
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }

    // Sort Dialog
    if (state.showSortDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(MovieListEvent.ToggleSortDialog) },
            title = { Text("Sort Movies") },
            text = {
                Column {
                    ListItem(
                        headlineContent = { Text("By Title") },
                        leadingContent = {
                            RadioButton(
                                selected = state.sortType is MovieSortType.Title,
                                onClick = { viewModel.onEvent(MovieListEvent.Sort(MovieSortType.Title)) }
                            )
                        }
                    )
                    ListItem(
                        headlineContent = { Text("By Release Date") },
                        leadingContent = {
                            RadioButton(
                                selected = state.sortType is MovieSortType.ReleaseDate,
                                onClick = { viewModel.onEvent(MovieListEvent.Sort(MovieSortType.ReleaseDate)) }
                            )
                        }
                    )
                    ListItem(
                        headlineContent = { Text("By Rating") },
                        leadingContent = {
                            RadioButton(
                                selected = state.sortType is MovieSortType.Rating,
                                onClick = { viewModel.onEvent(MovieListEvent.Sort(MovieSortType.Rating)) }
                            )
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onEvent(MovieListEvent.ToggleSortDialog) }
                ) {
                    Text("OK")
                }
            }
        )
    }
}