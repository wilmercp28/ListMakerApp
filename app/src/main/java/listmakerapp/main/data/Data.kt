package listmakerapp.main.data

import java.time.LocalDateTime
import java.util.UUID


data class AppData(
    val list: List<ListOfItems>?,
    val selectedIndex: Int,
    val isShoppingMode: Boolean
)

data class ListOfItems(
    val id: String = UUID.randomUUID().toString(),
    var name: String = "New List",
    var items: List<Item> = emptyList(),
    val dateOfCreation: String = LocalDateTime.now().toString()
)


data class Item(
    val id: String = UUID.randomUUID().toString(),
    val unit: String = "Pieces",
    val price: String = "",
    val category: String = "Category",
    val quantity: String = "",
    val description: String = ""
)