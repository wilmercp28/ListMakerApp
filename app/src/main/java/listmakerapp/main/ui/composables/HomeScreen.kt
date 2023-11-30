package listmakerapp.main.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.MutableLiveData
import listmakerapp.main.data.ListOfItems


@Composable
fun HomeScreen(list: MutableLiveData<List<ListOfItems>>, selectedIndex: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = list.value!![selectedIndex].name)
        Button(onClick = { list.value!![selectedIndex].name = "Clicked"}) {
            Text(text = "Test")
        }
    }
}