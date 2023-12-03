package listmakerapp.main.ui.composablesScreens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import listmakerapp.main.viewModels.ShareViewModel

@Composable
fun EditModeScreen(
    navController: NavHostController,
    shareViewModel: ShareViewModel = viewModel()
) {

    val list = shareViewModel.listState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {







    }
}