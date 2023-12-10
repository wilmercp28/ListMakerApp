package listmakerapp.main.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import listmakerapp.main.data.AppData
import listmakerapp.main.data.Item
import listmakerapp.main.data.ListOfItems
import java.time.LocalDateTime

class ShareViewModel : ViewModel() {


    private val _list = MutableStateFlow<List<ListOfItems>>(emptyList())
    private val _selectedIndex = MutableStateFlow(0)
    private val _isShoppingMode = MutableStateFlow(false)
    private val _loadingAppState = MutableStateFlow(true)
    private val _isEditMode = MutableStateFlow(false)
    private val _groupingBy = MutableStateFlow("Name")
    private var _sortingBy = MutableStateFlow("Last Modify")


    val listState: StateFlow<List<ListOfItems>> get() = _list
    val selectedIndex: StateFlow<Int> get() = _selectedIndex
    val isShoppingMode: StateFlow<Boolean> get() = _isShoppingMode
    val loadingAppState: StateFlow<Boolean> get() = _loadingAppState
    val editMode: StateFlow<Boolean> get() = _isEditMode
    val groupingBy: StateFlow<String> get() = _groupingBy
    val sortingBy: StateFlow<String> get() = _sortingBy

    val groceryUnits = listOf(
        "Pounds", "Ounces", "Grams", "Kilograms",
        "Liters", "Milliliters",
        "Pieces", "Bunches", "Bags", "Boxes",
        "Gallons", "Quarts", "Pints", "Fluid Ounces",
        "Dozens", "Cans", "Jars", "Bottles"
    )
    val groceryCategories = listOf(
        "Cat",
        "Produce",
        "Dairy",
        "Bakery",
        "Meat",
        "Seafood",
        "Frozen",
        "Canned",
        "Pasta",
        "Breakfast",
        "Condiments",
        "Snacks",
        "Beverages",
        "Deli",
        "Baking",
        "Canned/Jarred",
        "Household",
        "Personal Care",
        "Baby Care",
        "Pet Supplies",
        "Health",
        "International",
        "Organic",
        "Gluten-Free",
        "Gourmet",
        "Bakery",
        "Herbs/Spices",
        "Desserts",
        "Ethnic",
        "Nuts/Seeds",
        "Grilling",
        "Chips",
        "Soup",
        "Coffee",
        "Juices",
        "Paper",
        "Kitchenware",
        "Office",
        "Home Decor",
        "Party",
        "Condiments"
    )
    val listOfSorting = listOf(
        "Last Modify",
        "# of items",
        "Name"
    )
    val listGrouping = listOf(
        "Name",
        "Category"
    )


    //State Changing Functions
    fun loadStates(appData: AppData) {
        _list.value = appData.list ?: emptyList()
        _selectedIndex.value = appData.selectedIndex
        _isShoppingMode.value = appData.isShoppingMode
    }

    fun changeLoadingState() {
        _loadingAppState.value = !_loadingAppState.value
    }

    fun changeShoppingMode() {
        _isShoppingMode.value = !_isShoppingMode.value
    }

    fun changeEditMode() {
        _isEditMode.value = !_isEditMode.value
    }

    fun changeSelectedIndex(index: Int) {
        _selectedIndex.value = index
    }

    fun changeSorting(
        sorting: String
    ){
        _sortingBy.value = sorting
    }
    fun changeGrouping(
        grouping: String
    ){
        _groupingBy.value = grouping
    }


    //Change list
    fun addNewList() {
        val mutableList = _list.value.toMutableList()
        Log.d("Newlist", mutableList.toString())
        mutableList += ListOfItems()
        _list.value = mutableList
    }

    fun removeList(listItem: ListOfItems) {
        val mutableList = _list.value.toMutableList()
        mutableList -= listItem
        _list.value = mutableList
    }

    fun changeListName(newName: String) {
        val mutableList = _list.value.toMutableList()
        mutableList[_selectedIndex.value] = mutableList[_selectedIndex.value].copy(
            name = newName,
            dateOfLastModify = LocalDateTime.now().toString()
        )
        _list.value = mutableList
    }

    fun changeFavoriteState(listItem: ListOfItems) {
        val index = _list.value.indexOf(listItem)
        val mutableList = _list.value.toMutableList()
        mutableList[index] = mutableList[index].copy(favorite = !mutableList[index].favorite)
        _list.value = mutableList
    }

    //Change items on list
    fun addItem() {
        val mutableList = _list.value.toMutableList()
        mutableList[_selectedIndex.value] = mutableList[_selectedIndex.value].copy(
            items = mutableList[_selectedIndex.value].items + Item(),
            dateOfLastModify = LocalDateTime.now().toString()
        )
        _list.value = mutableList
    }

    fun changeItem(item: Item, changedItem: Item) {
        val indexOfItem = _list.value[_selectedIndex.value].items.indexOfFirst { it.id == item.id }

        if (indexOfItem >= 0) {
            val mutableList = _list.value.toMutableList()
            val mutableItem = _list.value[_selectedIndex.value].items.toMutableList()
            mutableItem[indexOfItem] = changedItem
            mutableList[_selectedIndex.value] = mutableList[_selectedIndex.value].copy(
                items = mutableItem,
                dateOfLastModify = LocalDateTime.now().toString()
            )
            _list.value = mutableList
        } else {
            Log.e("ShareViewModel", "changeItem: Item not found in the list")
        }
    }


    fun deleteItem(item: Item) {
        val selectedIndex = _selectedIndex.value
        val currentList = _list.value
        val currentItems = currentList[selectedIndex].items
        val indexOfItem = currentItems.indexOfFirst { it.id == item.id }

        val updatedItems = currentItems.filterIndexed { index, _ -> index != indexOfItem }

        val updatedList = currentList.toMutableList()
        updatedList[selectedIndex] = updatedList[selectedIndex].copy(
            items = updatedItems,
            dateOfLastModify = LocalDateTime.now().toString()
        )

        _list.value = updatedList
    }


}