package listmakerapp.main.ui.composables

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import listmakerapp.main.data.Item
import listmakerapp.main.viewmodel.ListViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ListEditing(
    navHost: NavHostController,
    listState: ListViewModel
) {
    val list by listState.list.collectAsState()
    val selectedIndex = listState.selectedIndex.collectAsState()
    var temporalListName by rememberSaveable { mutableStateOf(list[selectedIndex.value].name) }
    val listItems = list[selectedIndex.value].items


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    OutlinedTextField(
                        value = temporalListName,
                        onValueChange = { newName -> temporalListName = newName },
                        modifier = Modifier
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    listState.changeListName(
                                        list[selectedIndex.value],
                                        temporalListName
                                    )
                                }
                            }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navHost.navigate("HOME") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { scaffoldPaddling ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPaddling)
        ) {
            val itemModifier = Modifier
                .align(Alignment.CenterHorizontally)
                .border(
                    1.dp, MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(10.dp)
                )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(5.dp),
                content = {
                    items(list[selectedIndex.value].items, key = { it.id }) { item ->
                        Box(modifier = Modifier
                            .animateItemPlacement()
                            .padding(5.dp)) {
                            ShowItem(
                                item,
                                itemModifier,
                                onItemChange = {newItem ->
                                    listState.changeItem(item,newItem)
                                },
                                onItemRemove = { item ->
                                    listState.removeItem(selectedIndex.value, item)
                                }
                            )
                        }
                    }
                    item {
                        Box(
                            modifier = itemModifier.animateItemPlacement(tween(2000)),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = {
                                    listState.addItem()
                                }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add New Item")
                            }
                        }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ShowItem(
    item: Item,
    modifier: Modifier,
    onItemChange: (Item) -> Unit,
    onItemRemove: (Item) -> Unit
) {
    var tempQuantity by rememberSaveable { mutableStateOf(item.quantity) }
    var tempDescription by rememberSaveable { mutableStateOf(item.description) }
    val tempUnitType by rememberSaveable { mutableStateOf(item.unit) }
    val textStyle = androidx.compose.ui.text.TextStyle(
        fontSize = 20.sp,
    )

    fun onFocusLost() {
        onItemChange(
            item.copy(
                unit = tempUnitType,
                quantity = tempQuantity,
                description = tempDescription
            )
        )
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(onClick = { /*TODO*/ }, modifier = Modifier) {
                Text(text = tempUnitType, style = textStyle)
            }
            OutlinedTextField(
                value = tempQuantity,
                onValueChange = { newQuantity -> tempQuantity = newQuantity },
                maxLines = 1,
                label = { Text(text = "Quantity") },
                textStyle = textStyle,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        if (!it.isFocused) {
                            onFocusLost()
                            keyboardController?.hide()
                        }
                    }
            )

            IconButton(onClick = { onItemRemove(item) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Item", tint = Color.Red)
            }
        }
        OutlinedTextField(
            value = tempDescription,
            onValueChange = { newDescription -> tempDescription = newDescription },
            maxLines = 1,
            textStyle = textStyle,
            label = { Text(text = "Description") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .onFocusChanged {
                    if (!it.isFocused) {
                        onFocusLost()
                        keyboardController?.hide()
                    }
                }
                .padding(10.dp)
                .fillMaxWidth()
        )
    }
}

