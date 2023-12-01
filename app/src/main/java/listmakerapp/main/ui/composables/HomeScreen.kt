package listmakerapp.main.ui.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import listmakerapp.main.ListViewModel


@Composable
fun HomeScreen(selectedIndex: Int) {
    val viewModel = ListViewModel()
    val name by remember { mutableStateOf(viewModel.list[0].name) }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = name)

            Button(onClick = {
                viewModel.changeName("New Name")
            }) {
                Text(text = "sssss")
            }
        }
}