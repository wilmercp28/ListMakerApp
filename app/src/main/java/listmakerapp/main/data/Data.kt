package listmakerapp.main.data

import androidx.compose.runtime.MutableState


data class ListOfItems(
    var name: String,
    val items: List<Item>
)



data class Item(
    val unit: String,
    val quantity: String,
    val description: String,
)