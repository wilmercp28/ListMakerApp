package listmakerapp.main.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import listmakerapp.main.data.ListOfItems
import java.time.LocalDate

class ShareViewModel : ViewModel() {


    private val _list = MutableStateFlow<List<ListOfItems>>(emptyList())
    private val _selectedIndex = MutableStateFlow(0)
    private val _isShoppingMode = MutableStateFlow(false)
    private val _loadingAppState = MutableStateFlow(true)


    val listState: StateFlow<List<ListOfItems>> get() = _list
    val selectedIndex: StateFlow<Int> get() = _selectedIndex
    val isShoppingMode: StateFlow<Boolean> get() = _isShoppingMode
    val loadingAppState: StateFlow<Boolean> get() = _loadingAppState


    //State Changing Functions
    fun changeLoadingState() {
        _loadingAppState.value = !_loadingAppState.value
    }

    fun changeShoppingMode() {
        _isShoppingMode.value = !_isShoppingMode.value
    }


    //Changing List
    fun addNewList() {
        val mutableList = _list.value.toMutableList()
        mutableList += ListOfItems(
            name = "New List",
            items = emptyList(),
            dateOfCreation = LocalDate.now()
        )
        _list.value = mutableList
    }


}