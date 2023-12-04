package listmakerapp.main.ui.composablesScreens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import listmakerapp.main.viewModels.ShareViewModel

@Composable
fun LoadingScreen(navController: NavHostController, shareViewModel: ShareViewModel) {
    val loadingState = shareViewModel.loadingAppState.collectAsState()
    if (loadingState.value) {
    navController.navigate("HOME")
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}