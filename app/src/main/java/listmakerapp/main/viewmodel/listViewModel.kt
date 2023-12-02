package listmakerapp.main.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import listmakerapp.main.data.Item
import listmakerapp.main.data.ListOfItems


open class ListViewModel(
) : ViewModel() {


    private val _selectedIndex = MutableStateFlow(0)
    private val _loadingState = MutableStateFlow(true)
    private val _list = MutableStateFlow(listOf<ListOfItems>())
    val list = _list.asStateFlow()
    val loadingState = _loadingState.asStateFlow()
    val selectedIndex = _selectedIndex.asStateFlow()


    fun changeSelectedIndex(
        index: Int
    ) {
        _selectedIndex.value = index
    }

    fun changeItem(
        item: Item,
        newItem: Item
    ) {
        val index = _list.value[_selectedIndex.value].items.indexOf(item)
        val mutableList = _list.value.toMutableList()
        val mutableItems = mutableList[_selectedIndex.value].items.toMutableList()
        mutableItems[index] = newItem
        mutableList[_selectedIndex.value] = mutableList[_selectedIndex.value].copy(
            items = mutableItems
        )
        _list.value = mutableList

    }

    fun addItem() {
        val mutableList = _list.value.toMutableList()
        val mutableItems = mutableList[_selectedIndex.value].items.toMutableList()
        mutableItems += Item(unit = "Unit", quantity = "", description = "")
        mutableList[_selectedIndex.value] =
            mutableList[_selectedIndex.value].copy(items = mutableItems)
        _list.value = mutableList
    }

    fun removeItem(
        selectedIndex: Int,
        item: Item
    ) {
        val mutableList = _list.value.toMutableList()
        val mutableItems = mutableList[selectedIndex].items.toMutableList()
        mutableItems -= item
        mutableList[selectedIndex] =
            mutableList[selectedIndex].copy(items = mutableItems)
        _list.value = mutableList

    }

    fun changeLoadingState() {
        _loadingState.value = false
    }

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