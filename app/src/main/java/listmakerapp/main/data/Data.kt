package listmakerapp.main.data

import java.util.Date
import java.util.UUID


data class AppData(
    val list: List<ListOfItems>,
    val selectedIndex: Int,
    val isShoppingMode: Int
)
data class ListOfItems(
    var name: String,
    var items: List<Item>,
    val dateOfCreation: Date
)


data class Item(
    val id: String = UUID.randomUUID().toString(),
    val unit: String,
    val quantity: String,
    val description: String
)