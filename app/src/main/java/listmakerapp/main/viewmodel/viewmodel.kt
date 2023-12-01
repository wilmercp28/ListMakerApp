package listmakerapp.main.viewmodel


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import listmakerapp.main.data.ListOfItems

class ListViewModel(
) : ViewModel() {

    private val _loadingState = MutableStateFlow(true)
    val loadingState: StateFlow<Boolean> get() = _loadingState

    private val _list = MutableStateFlow(listOf<ListOfItems>())
    val list = _list.asStateFlow()




    fun updateList(list: List<ListOfItems>) {
        if (list.isNullOrEmpty()) {
            _list.value = list
        } else {
            _list.value = emptyList()
        }
        _loadingState.value = false
    }

    fun changeListName(list: ListOfItems, newName: String) {
        val index = _list.value.indexOf(list)
        val mutableList = _list.value.toMutableList()
        mutableList[index] = mutableList[index].copy(name = newName)
        _list.value = mutableList
    }
    fun addNewList(

    ){
        _list.value += ListOfItems(name = "pepe" , items = emptyList())

    }


}