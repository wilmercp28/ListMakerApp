package listmakerapp.main.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import listmakerapp.main.data.Item
import listmakerapp.main.viewmodel.ListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListEditing(
    navHost: NavHostController,
    listState: ListViewModel,
    selectedIndex: MutableIntState
) {
    val list by listState.list.collectAsState()
    var name by rememberSaveable { mutableStateOf(list[selectedIndex.intValue].name) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .onFocusChanged { focusState ->
                                if (!focusState.isFocused) {
                                    listState.changeListName(
                                        list[selectedIndex.intValue],
                                        name
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
                .fillMaxWidth()
                .padding(10.dp)
                .padding(scaffoldPaddling)
        ) {
            val quantityModifier = Modifier.weight(1f)
            val descriptionModifier = Modifier.weight(2f)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp)),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quantity",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = quantityModifier
                )
                Text(
                    text = "Description",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = descriptionModifier
                )
            }
            LazyColumn(
                modifier = Modifier
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    items(list[selectedIndex.intValue].items, key = { it.id }) { item ->
                        ShowItem(
                            item,
                            quantityModifier,
                            descriptionModifier,
                            onItemChange = {},
                            onItemRemove = {}
                        )
                    }
                    item {
                        IconButton(
                            onClick = {
                                      listState.addItem(selectedIndex)
                            }, modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp, MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(10.dp)
                                )
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add New Item")
                        }
                    }
                }
            )
        }

    }
}

@Composable
fun ShowItem(
    item: Item,
    quantityModifier: Modifier,
    descriptionModifier: Modifier,
    onItemChange: () -> Unit,
    onItemRemove: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

        }
    }


}