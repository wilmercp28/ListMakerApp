package listmakerapp.main.ui.composablesScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import listmakerapp.main.data.ListOfItems
import listmakerapp.main.viewModels.ShareViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    shareViewModel: ShareViewModel = viewModel()
) {
    val list by shareViewModel.listState.collectAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    SearchListBar()
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { shareViewModel.addNewList() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add New List")
            }
        }
    ) { scaffoldPaddling ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPaddling)
        ) {
            ListColumn(list)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchListBar(

) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        shape = RoundedCornerShape(100)
    )

}

@Composable
fun ListColumn(list: List<ListOfItems>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(5.dp),
        content = {
            items(list, key = { it.id }) { list ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(20)),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = list.name, fontSize = 30.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    )
}

