package listmakerapp.main.data

import java.util.UUID


data class AppData(
    val list: List<ListOfItems>
)
data class ListOfItems(
    var name: String,
    var items: List<Item>
)


data class Item(
    val id: String = UUID.randomUUID().toString(),
    val unit: String,
    val quantity: String,
    val description: String
)