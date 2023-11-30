package listmakerapp.main


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import listmakerapp.main.data.Item
import listmakerapp.main.data.ListOfItems

class ViewModel : ViewModel() {
    private val _list = MutableLiveData<List<ListOfItems>>()
    val list: LiveData<List<ListOfItems>> get() = _list

    init {
        _list.value = mutableListOf(
            ListOfItems(
                "Test",
                listOf(
                    Item("Unit", "1", "Hola"),
                    Item("Unit", "1", "Hola"),
                    Item("Unit", "1", "Hola"),
                    Item("Unit", "1", "Hola"),
                    Item("Unit", "1", "Hola"),
                    Item("Unit", "1", "Hola")
                )
            )
        )
    }
    fun updateName(index: Int, newName: String) {
        val currentList = _list.value.orEmpty().toMutableList()
        currentList[index].name = newName
        _list.value = currentList
    }
}