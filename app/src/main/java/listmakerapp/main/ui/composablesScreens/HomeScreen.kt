package listmakerapp.main.ui.composablesScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import listmakerapp.main.data.ListOfItems
import listmakerapp.main.ui.RemoveConfirmationAlert
import listmakerapp.main.viewModels.ShareViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    shareViewModel: ShareViewModel
) {
    val list by shareViewModel.listState.collectAsState()
    var selectedList by remember { mutableStateOf<ListOfItems?>(null) }
    val lazyColumnState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()
    var settingsMenuExpanded by remember { mutableStateOf(false) }
    val sortingList = shareViewModel.listOfSorting
    var sortingBy by remember { mutableStateOf(sortingList[0]) }

    val sortList = when (sortingBy) {
        "Last Modify" -> list.sortedBy { it.dateOfLastModify }
        "Name" -> list.sortedBy { it.name }
        "# of items" -> list.sortedBy { it.items.size.toString() }
        "Favorite" -> list.sortedBy { it.favorite }
        else -> list.sortedBy { it.dateOfLastModify }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    SearchListBar()
                },
                actions = {
                    IconButton(onClick = { settingsMenuExpanded = !settingsMenuExpanded }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                        SettingsMenu(
                            settingsMenuExpanded,
                            onDismiss = { settingsMenuExpanded = !settingsMenuExpanded },
                            sortingBy,
                            sortingList,
                            onSortingChange = { sortingBy = it }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                shareViewModel.addNewList()
                coroutine.launch {
                    if (list.isNotEmpty()) {
                        lazyColumnState.scrollToItem(0)
                    }
                }

            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add New List")
            }
        }
    ) { scaffoldPaddling ->
        LazyColumn(
            state = lazyColumnState,
            modifier = Modifier
                .padding(scaffoldPaddling)
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(5.dp),
            content = {
                if (list.isNotEmpty()) {
                    items(sortList, key = { it.id }) { listItem ->
                        Box(
                            modifier = Modifier
                                .animateItemPlacement()
                        ) {
                            ShowList(
                                listItem,
                                selectedList == listItem,
                                onSelection = {
                                    selectedList = if (selectedList != listItem) listItem else null
                                },
                                onRemove = { shareViewModel.removeList(listItem) },
                                onEditMode = {
                                    shareViewModel.changeSelectedIndex(list.indexOf(listItem))
                                    shareViewModel.changeEditMode()
                                    navController.navigate("EDIT-MODE")
                                },
                                onShoppingMode = {}
                            )
                        }
                    }
                }
            }
        )
    }
}


@Composable
fun SettingsMenu(
    settingsMenuExpanded: Boolean,
    onDismiss: () -> Unit,
    sortingBy: String,
    sortList: List<String>,
    onSortingChange: (String) -> Unit
) {
    DropdownMenu(
        modifier = Modifier
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(10)),
        expanded = settingsMenuExpanded,
        onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "SortBy")
            Divider()
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                for (sorting in sortList) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = sorting)
                        RadioButton(
                            selected = sorting == sortingBy,
                            onClick = { onSortingChange(sorting) })
                    }
                }
            }
            Divider()
        }
    }
}


@Composable
fun ShowList(
    listItem: ListOfItems,
    expanded: Boolean,
    onSelection: () -> Unit,
    onRemove: () -> Unit,
    onEditMode: () -> Unit,
    onShoppingMode: () -> Unit
) {
    var showRemoveAlert by remember { mutableStateOf(false) }
    if (showRemoveAlert) {
        val title = "Are you sure you want to delete ${listItem.name}?"
        RemoveConfirmationAlert(
            title = title,
            onConfirm = {
                onRemove()
                showRemoveAlert = false
            },
            onDismissive = { showRemoveAlert = false }
        )
    }
    Column(
        modifier = Modifier
            .clickable {
                onSelection()
            }
            .fillMaxWidth()
            .height(100.dp)
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(20)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = listItem.name,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxSize(),
            visible = !expanded
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val creationDate = listItem.dateOfLastModify
                val parsedCreationDate = LocalDateTime.parse(
                    creationDate,
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                )
                val duration = ChronoUnit.MINUTES.between(parsedCreationDate, LocalDateTime.now())
                val durationText = when {
                    duration < 60 -> "Last modify ${duration}m ago"
                    duration < 1440 -> "Last modify \$${duration / 60}h ago"
                    duration < 10080 -> "Last modify \$${duration / 1440}d ago"
                    duration < 43800 -> "Last modify \$${duration / 10080}w ago"
                    duration < 525600 -> "Last modify \$${duration / 43800}mo ago"
                    else -> "Last modify \$${duration / 525600}y ago"
                }
                Text(
                    text = listItem.items.size.toString(),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = durationText,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxSize(),
            visible = expanded
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    showRemoveAlert = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove List",
                        tint = Color.Red
                    )
                }
                IconButton(onClick = { onEditMode() }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit List")
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchListBar(

) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(0.8f),
        value = searchQuery,
        onValueChange = { searchQuery = it },
        shape = RoundedCornerShape(100),
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = { searchQuery = "" },
                    modifier = Modifier
                        .size(30.dp)
                ) {
                    Text(text = "X")
                }
            }
        },
        singleLine = true,
        placeholder = { Text(text = "List name") }
    )

}




