package listmakerapp.main.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import listmakerapp.main.data.ListOfItems

class ListViewModel(
) : ViewModel() {


    private val _loadingState = MutableStateFlow(true)
    private val _list = MutableStateFlow(listOf<ListOfItems>())
    val list = _list.asStateFlow()


    fun updateList(list: List<ListOfItems>?) {
        if (!list.isNullOrEmpty()) {
            _list.value = list
        } else {
            _list.value = emptyList()
        }
        _loadingState.value = false
    }

    fun removeList(listOfItems: ListOfItems) {
        val mutableList = _list.value.toMutableList()
        val index = mutableList.indexOf(listOfItems)

        if (index != -1) {
            mutableList.removeAt(index)
            _list.value = mutableList
            Log.d("RemoveList", "Item removed successfully")
        } else {
            Log.d("RemoveList", "Item not found in the list")
        }
    }

    fun changeListName(list: ListOfItems, newName: String) {
        val index = _list.value.indexOf(list)
        val mutableList = _list.value.toMutableList()
        mutableList[index] = mutableList[index].copy(name = newName)
        _list.value = mutableList
    }

    fun addNewList(

    ) {
        var listNumber = 1
        var name = "New List $listNumber"

        while (nameAlreadyExist(name)) {
            listNumber++
            name = "New List $listNumber"
        }
        _list.value += ListOfItems(name = name, items = emptyList())
    }

    private fun nameAlreadyExist(
        name: String
    ): Boolean {
        return _list.value.any { it.name == name }

    }


}