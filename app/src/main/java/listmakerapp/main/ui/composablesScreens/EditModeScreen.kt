package listmakerapp.main.ui.composablesScreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import listmakerapp.main.data.Item
import listmakerapp.main.viewModels.ShareViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditModeScreen(
    navController: NavHostController,
    shareViewModel: ShareViewModel
) {
    val index = shareViewModel.selectedIndex.collectAsState().value
    val list = shareViewModel.listState.collectAsState().value
    val selectedList = list[index]
    val listOfGroseriesUnits = shareViewModel.groceryUnits

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                title = {
                    TextField(
                        textField = selectedList.name,
                        label = "List Name",
                        fillMaxWidthFloat = 0.9f,
                        onChange = { newName -> shareViewModel.changeListName(newName) },
                    )
                },
                navigationIcon = {

                    IconButton(onClick = { navController.navigate("HOME") }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { scaffoldPaddling ->
        LazyColumn(
            modifier = Modifier
                .padding(scaffoldPaddling)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = {
                items(selectedList.items, key = { it.id }) { item ->
                    Box(modifier = Modifier.animateItemPlacement()) {
                        ShowItem(
                            item = item,
                            listOfGroseriesUnits,
                            onChange = { changedItem ->
                                shareViewModel.changeItem(
                                    item,
                                    changedItem
                                )
                            },
                            onDelete = {
                                shareViewModel.deleteItem(item)
                            })
                    }
                }
                item {
                    IconButton(
                        onClick = { shareViewModel.addItem() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                            .animateItemPlacement()
                    ) {
                        Text(text = "Add New Item")
                    }
                }
            }
        )


    }
}

@Composable
fun ShowItem(
    item: Item,
    listOfGroceriesUnits: List<String>,
    onChange: (Item) -> Unit,
    onDelete: () -> Unit
) {
    var expand by remember { mutableStateOf(false) }
    var selectedUnitType by remember { mutableStateOf(item.unit) }
    var quantity = item.quantity
    var description = item.description

    fun change() {
        onChange(item.copy(unit = selectedUnitType, quantity = quantity, description = description))
    }
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(20))
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { expand = !expand },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = selectedUnitType)
            }

            DropdownMenu(
                expanded = expand,
                onDismissRequest = { expand = false }
            ) {
                listOfGroceriesUnits.forEach { unitType ->
                    DropdownMenuItem(
                        text = { Text(text = unitType) },
                        onClick = {
                            selectedUnitType = unitType
                            expand = false
                            change()
                        }
                    )
                }
            }
            TextField(
                onChange = { quantity = it;change() },
                label = "Quantity",
                textField = quantity,
                fillMaxWidthFloat = 0.7f
            )
            IconButton(onClick = { onDelete() }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Item",
                    tint = Color.Red
                )
            }
        }
        TextField(
            onChange = { description = it;change() },
            label = "Description",
            textField = description
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TextField(
    textField: String = "",
    label: String = "",
    height: Dp = 60.dp,
    fillMaxWidthFloat: Float = 1f,
    textStyle: TextStyle = TextStyle(fontSize = 15.sp),
    onChange: (String) -> Unit

) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf(textField) }
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .onFocusChanged {
                if (!it.isFocused) {
                    onChange(text)
                    keyboardController?.hide()
                }
            }
            .height(height)
            .fillMaxWidth(fillMaxWidthFloat),
        shape = RoundedCornerShape(100),
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        }),
        textStyle = textStyle
    )


}