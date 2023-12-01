package listmakerapp.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
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
import listmakerapp.main.ui.theme.ListMakerAppTheme
import listmakerapp.main.viewmodel.ListViewModel

class MainActivity : ComponentActivity() {

    private val listViewModel: ListViewModel by viewModels()
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "AppState")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val appState = dataStore.getAppData().first()
            Log.d("LoadData",appState.toString())
            appState.list?.let { listViewModel.updateList(it) }
        }

        setContent {
            val navController = rememberNavController()
            ListMakerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val selectedIndex by remember { mutableIntStateOf(0) }
                    NavHost(navController, startDestination = "HOME") {
                        composable("HOME") {
                            HomeScreen(listViewModel, selectedIndex)
                        }
                    }
                }
            }
        }
    }


    override fun onStop() {
        super.onStop()
        lifecycleScope.launch {
            val appState = AppData(
                list = listViewModel.list.value
            )
            dataStore.saveAppData(appState)
            Log.d("Saved","Save")
        }
    }
    override fun onDestroy() {
        super.onDestroy()

    }

}

