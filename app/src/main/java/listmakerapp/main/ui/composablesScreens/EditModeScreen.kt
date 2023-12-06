package listmakerapp.main.ui.composablesScreens

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import listmakerapp.main.data.Item
import listmakerapp.main.viewModels.ShareViewModel
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditModeScreen(
    navController: NavHostController,
    shareViewModel: ShareViewModel
) {
    val index = shareViewModel.selectedIndex.collectAsState().value
    val list = shareViewModel.listState.collectAsState().value
    val selectedList = list[index]
    val listOfGroceriesUnits = shareViewModel.groceryUnits
    val listOfCategories = shareViewModel.groceryCategories

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
                            listOfGroceriesUnits,
                            listOfCategories,
                            onChange = { changedItem ->
                                shareViewModel.changeItem(
                                    item,
                                    changedItem
                                )
                                Log.d("ChangeItem index", selectedList.items.indexOfFirst { it.id == item.id }.toString())
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
                            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10))
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
    listOfCategories: List<String>,
    onChange: (Item) -> Unit,
    onDelete: () -> Unit
) {
    var expandUnitType by remember { mutableStateOf(false) }
    var expandCategory by remember { mutableStateOf(false) }
    var selectedUnitType by remember { mutableStateOf(item.unit) }
    var selectedCategory by remember { mutableStateOf(item.category) }
    var quantity = item.quantity
    var description = item.description
    var price = item.price

    fun change() {
        onChange(
            item.copy(
                unit = selectedUnitType,
                quantity = quantity,
                description = description,
                price = price,
                category = selectedCategory
            )
        )
    }
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(10))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onDelete() }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Item",
                    tint = Color.Red
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { expandUnitType = !expandUnitType },
                modifier = Modifier.weight(0.5f)
            ) {
                Text(text = selectedUnitType)
            }
            DropdownMenu(
                expanded = expandUnitType,
                onDismissRequest = { expandUnitType = false }
            ) {
                listOfGroceriesUnits.sortedBy { it }.forEach { unitType ->
                    DropdownMenuItem(
                        text = { Text(text = unitType) },
                        onClick = {
                            selectedUnitType = unitType
                            expandUnitType = false
                            change()
                        }
                    )
                }
            }
            TextField(
                onChange = { quantity = it;change() },
                label = "Quantity",
                textField = quantity,
                fillMaxWidthFloat = 0.5f,
                keyboardType = KeyboardType.Number
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { expandCategory = !expandCategory },
                modifier = Modifier.weight(0.5f)
            ) {
                Text(text = selectedCategory)
            }
            DropdownMenu(
                expanded = expandCategory,
                onDismissRequest = { expandCategory = false }
            ) {
                listOfCategories.sortedBy { it }.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(text = category) },
                        onClick = {
                            selectedCategory = category
                            expandCategory = false
                            change()
                        }
                    )
                }
            }
            TextField(
                onChange = { price = it;change() },
                label = "Price",
                textField = price,
                fillMaxWidthFloat = 0.5f,
                keyboardType = KeyboardType.Number
            )

        }
        TextField(
            onChange = { description = it;change() },
            label = "Description",
            textField = description
        )
        Spacer(modifier = Modifier.size(20.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TextField(
    textField: String = "",
    label: String = "",
    charLimit: Int = 9999999,
    keyboardType: KeyboardType = KeyboardType.Text,
    fillMaxWidthFloat: Float = 1f,
    textStyle: TextStyle = TextStyle(fontSize = 15.sp),
    onChange: (String) -> Unit

) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf(textField) }
    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it.take(charLimit)
        },
        modifier = Modifier
            .onFocusChanged {
                if (!it.isFocused) {
                    onChange(text)
                    keyboardController?.hide()
                }
            }
            .fillMaxWidth(fillMaxWidthFloat)
            .animateContentSize(),
        shape = RoundedCornerShape(10),
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        }),
        textStyle = textStyle
    )
}