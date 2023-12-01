package listmakerapp.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import listmakerapp.main.data.AppData
import listmakerapp.main.data.getAppData
import listmakerapp.main.data.saveAppData
import listmakerapp.main.ui.composables.HomeScreen
import listmakerapp.main.ui.composables.ListEditing
import listmakerapp.main.ui.theme.ListMakerAppTheme
import listmakerapp.main.viewmodel.ListViewModel

class MainActivity : ComponentActivity() {
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "AppState")
    private val listViewModel: ListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val appData = getAppData(dataStore).first()
            listViewModel.updateList(appData.list)
        }
        setContent {
            val navController = rememberNavController()
            val selectedIndex = remember { mutableIntStateOf(0) }
            ListMakerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController, startDestination = "HOME") {
                        composable("HOME") {
                            HomeScreen(navController,listViewModel, selectedIndex)
                        }
                        composable("EDIT"){
                            ListEditing(navController,listViewModel,selectedIndex)
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            val appState = AppData(
                list = listViewModel.list.value
            )
            saveAppData(appState, dataStore)
            Log.d("Saved", "Save")
        }
    }
}

