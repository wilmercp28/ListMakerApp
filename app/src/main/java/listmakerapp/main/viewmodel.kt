package listmakerapp.main


import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import listmakerapp.main.data.Item
import listmakerapp.main.data.ListOfItems

class ListViewModel : ViewModel() {
    private val _list = mutableListOf(ListOfItems(name = "hola", items = emptyList()))
    val list: MutableList<ListOfItems> = _list

    fun changeName(newName: String) {
        list[0].name = newName
    }
}