package listmakerapp.main.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import listmakerapp.main.data.ListOfItems
import listmakerapp.main.viewmodel.ListViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navHost: NavHostController, listViewModel: ListViewModel) {

    var selectedList by remember { mutableStateOf<ListOfItems?>(null) }
    val listState by listViewModel.list.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "List Maker") },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            SmallFloatingActionButton(onClick = {
                listViewModel.addNewList()
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add New List")
            }
        }


    ) { scaffoldPaddling ->
        val removedItems = remember { mutableListOf<ListOfItems>() }
        LazyColumn(
            modifier = Modifier
                .padding(scaffoldPaddling)
                .fillMaxSize(),
            content = {

                items(listState, key = { it.name }) { list ->
                    Box(
                        modifier = Modifier
                            .animateItemPlacement(
                                tween(1000)
                            )
                    ) {
                        ShowList(
                            list = list,
                            expanded = selectedList == list,
                            onToggle = {
                                selectedList = if (selectedList != it) it else null
                            },
                            onRemove = { list ->
                                removedItems += list
                                listViewModel.removeList(list)
                            },
                            onEdit = {
                                listViewModel.changeSelectedIndex(listState.indexOf(it))
                                navHost.navigate("EDIT")

                            }
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun ShowList(
    list: ListOfItems,
    expanded: Boolean,
    onToggle: (ListOfItems) -> Unit,
    onRemove: (ListOfItems) -> Unit,
    onEdit: (ListOfItems) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle(list) }
            .height(130.dp)
            .padding(10.dp)
            .background(
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(10.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = list.name, fontSize = 30.sp, color = MaterialTheme.colorScheme.onPrimary)
        Text(text = list.items.size.toString(), fontSize = 20.sp, color = MaterialTheme.colorScheme.onPrimary)

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandIn(),
            exit = fadeOut() + shrinkOut()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(onClick = { onRemove(list) }) {
                    Icon(Icons.Default.Delete, contentDescription = "RemoveList")
                }
                IconButton(onClick = { onEdit(list) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit List")
                }
            }
        }
    }
}