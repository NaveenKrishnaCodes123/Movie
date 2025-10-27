package com.example.movieapp


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.movieapp.domain.model.Movie
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MovieListItem(
    movie: Movie,
    onItemClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
    Card(
        onClick = onItemClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular Movie Poster
            AsyncImage(
                model = movie.poster_url,
                contentDescription = "Movie poster for ${movie.title}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Movie Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Release: ${formatDate(movie.release_date)}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Rating: ${movie.rating.toDouble()}/10",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Bookmark Button
            IconButton(onClick = onBookmarkClick) {
                Icon(
                    painter = painterResource(
                        id = if (movie.isBookmarked) {
                            R.drawable.ic_bookmark_filled
                        } else {
                            R.drawable.ic_bookmark_outline
                        }
                    ),
                    contentDescription = if (movie.isBookmarked) {
                        "Remove bookmark"
                    } else {
                        "Add bookmark"
                    },
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp * 1000) // API gives seconds → convert to milliseconds
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return sdf.format(date)
}