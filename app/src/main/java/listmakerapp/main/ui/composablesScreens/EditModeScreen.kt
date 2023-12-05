package listmakerapp.main.ui.composablesScreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
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
    val index by shareViewModel.selectedIndex.collectAsState()
    val list by shareViewModel.listState.collectAsState()
    val typesOfUnits = shareViewModel.groceryUnits
    val selectedList = list[index]

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
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
                .padding(scaffoldPaddling),
            content = {
                item {
                    Button(onClick = { shareViewModel.addItem() }) {
                        Text(
                            text = "Add Item",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                items(selectedList.items, key = { it.id }) { item ->
                    Box(modifier = Modifier.animateItemPlacement()) {
                        ShowItem(
                            item,
                            typesOfUnits,
                            onChange = { changedItem ->
                                shareViewModel.changeItem(
                                    item,
                                    changedItem
                                )
                            })
                    }
                }
            }
        )
    }
}


@Composable
fun ShowItem(item: Item, typesOfUnits: List<String>, onChange: (Item) -> Unit) {
    var expand by remember { mutableStateOf(false) }
    var selectedUnit by remember { mutableStateOf(item.unit) }
    var quantity = item.quantity
    var description = item.description
    fun changeItem() {
        onChange(item.copy(unit = selectedUnit, quantity = quantity, description = description))
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { expand = !expand },
            modifier = Modifier
        ) {
            Text(text = selectedUnit)
            DropdownMenu(expanded = expand, onDismissRequest = { expand = false; changeItem() }) {
                typesOfUnits.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(text = unit) },
                        onClick = { selectedUnit = unit; expand = false; changeItem() })
                }
            }
        }
        TextField(
            onChange = { quantity = it; changeItem() },
            fillMaxWidthFloat = 0.3f,
            textField = item.quantity,
            label = "Quantity"
        )
        TextField(
            onChange = { description = it; changeItem() },
            fillMaxWidthFloat = 1f,
            textField = item.description,
            label = "Description"
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