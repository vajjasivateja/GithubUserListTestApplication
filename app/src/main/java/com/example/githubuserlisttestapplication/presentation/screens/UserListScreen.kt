package com.example.githubuserlisttestapplication.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.githubuserlisttestapplication.domain.models.GithubUserModel
import com.example.githubuserlisttestapplication.presentation.screens.components.UserItem
import com.example.githubuserlisttestapplication.presentation.screens.viewmodel.NetworkViewModel
import com.example.githubuserlisttestapplication.presentation.screens.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun UserListScreen(
    userViewModel: UserViewModel = hiltViewModel(),
    networkViewModel: NetworkViewModel = hiltViewModel(),
    onUserClick: (GithubUserModel) -> Unit
) {

    var searchQuery by remember { mutableStateOf("") }

    val listUsers = userViewModel.userFeedPagingFlow.collectAsLazyPagingItems()

    val networkStatus = networkViewModel.getNetworkStatusLiveData().value ?: false

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = listUsers.loadState) {
        if (listUsers.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error ${(listUsers.loadState.refresh as LoadState.Error).error.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column {
//        if (!networkStatus) {
//            // No internet connection message
//            Text(
//                text = "No Internet Connection",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color.Red)
//                    .padding(8.dp),
//                color = Color.White,
//                textAlign = TextAlign.Center
//            )
//        }

        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                // Call the search function here or trigger a recomposition
                scope.launch {
                    userViewModel.setSearchQuery(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            label = { Text("Search by username or note") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    scope.launch {
                        userViewModel.setSearchQuery(searchQuery)
                    }
                }
            )
        )
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                listUsers.loadState.refresh is LoadState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                listUsers.loadState.append is LoadState.Loading -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(listUsers.itemCount) { index ->
                            listUsers[index]?.let {
                                UserItem(
                                    user = it,
                                    modifier = Modifier.fillMaxWidth(),
                                    onItemClick = { onUserClick(listUsers[index]!!) }
                                )
                            }
                        }
                        item {
                            CircularProgressIndicator()
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(1.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(listUsers.itemCount) { index ->
                            listUsers[index]?.let {
                                UserItem(
                                    user = it,
                                    modifier = Modifier.fillMaxWidth(),
                                    onItemClick = { onUserClick(listUsers[index]!!) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}