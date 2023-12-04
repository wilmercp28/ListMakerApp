package listmakerapp.main.ui.composablesScreens

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import listmakerapp.main.viewModels.ShareViewModel

@Composable
fun NavHostController(
    shareViewModel: ShareViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "LOADING-SCREEN") {
        composable(
            route = "LOADING-SCREEN",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            LoadingScreen()
            if (!shareViewModel.loadingAppState.collectAsState().value) navController.navigate("HOME")
        }
        composable("HOME") {
            Box(modifier = Modifier.padding(10.dp)) { HomeScreen(navController,shareViewModel) }
        }
        composable("EDIT-MODE") {
            Box(modifier = Modifier.padding(10.dp)) { EditModeScreen(navController,shareViewModel) }
        }
    }
}