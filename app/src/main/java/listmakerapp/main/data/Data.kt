package listmakerapp.main.data

import java.util.UUID


data class AppData(
    val list: List<ListOfItems>?,
    val selectedIndex: Int,
    val isShoppingMode: Boolean
)
data class ListOfItems(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var items: List<Item>,
    val dateOfCreation: String
)


data class Item(
    val id: String = UUID.randomUUID().toString(),
    val unit: String,
    val quantity: String,
    val description: String
)