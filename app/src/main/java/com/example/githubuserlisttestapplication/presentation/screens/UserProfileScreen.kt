package com.example.githubuserlisttestapplication.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.githubuserlisttestapplication.data.data_source.remote.dto.GithubUserProfileResponseDTO
import com.example.githubuserlisttestapplication.presentation.screens.viewmodel.UserProfileViewModel
import com.example.githubuserlisttestapplication.presentation.screens.viewmodel.UserViewModel
import com.example.githubuserlisttestapplication.utils.ResultWrapper
import kotlinx.coroutines.launch

@Composable
fun UserProfileScreen(
    userId: Int,
    navController: NavController,
    userViewModel: UserViewModel = hiltViewModel(),
    userProfileViewModel: UserProfileViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    var userDetails by remember { mutableStateOf<GithubUserProfileResponseDTO?>(null) }
    var loading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    // Trigger getUserById when the composable is first composed
    LaunchedEffect(userId) {
        userViewModel.getUserById(userId)
    }

    val user = userViewModel.user.value
    user?.let {
        scope.launch {
            val result = userProfileViewModel.getUserProfileByUsername(user.login)
            when (result) {
                is ResultWrapper.Success -> {
                    userDetails = result.data
                    loading = false
                }

                is ResultWrapper.Error -> {
                    // Handle error (e.g., display an error message)
                    loading = false
                }
            }
        }
        var note by remember { mutableStateOf(user.note ?: "") }
        // Display user information
        if (loading) {
            // Display a loading indicator while data is being fetched
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    AsyncImage(
                        model = ImageRequest.Builder(context = context).data(userDetails?.avatarUrl)
                            .build(),
                        contentDescription = user.login,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape),
                        alignment = Alignment.Center
                    )
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }

                item {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Followers: ${userDetails?.followers}",
                            style = TextStyle(fontSize = 14.sp, color = Color.Black)
                        )
                        Text(
                            text = "Following: ${userDetails?.following}",
                            style = TextStyle(fontSize = 14.sp, color = Color.Black)
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        elevation = 4.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Name:",
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.Start
                                )
                                Text(
                                    text = userDetails?.name ?: "",
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.End
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Company:",
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.Start
                                )
                                Text(
                                    text = userDetails?.company ?: "",
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.End
                                )
                            }
                            Spacer(modifier = Modifier.height(5.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Blog:",
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.Start
                                )
                                Text(
                                    text = userDetails?.blog ?: "",
                                    style = MaterialTheme.typography.labelLarge,
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }

                item {
                    // Text field to edit and save notes
                    Text(
                        text = "Notes:",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = note,
                        onValueChange = { newNote ->
                            note = newNote
                        },
                        label = { Text("Add Note") }
                    )

                    Button(
                        onClick = {
                            scope.launch {
                                if (note.isNotEmpty()) {
                                    userViewModel.updateNote(user.id, note)
                                }
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier
                            .padding(top = 16.dp)
                    ) {
                        Text(text = "Save Note")
                    }
                }
            }

        }
    }
}
