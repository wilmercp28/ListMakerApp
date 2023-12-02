package listmakerapp.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
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

    private val listViewModel: ListViewModel by viewModels()
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "AppState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val appData = getAppData(dataStore).first()
            listViewModel.updateList(appData.list)
            listViewModel.changeLoadingState()
        }

        setContent {
            val navController = rememberNavController()
            ListMakerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController, startDestination = "HOME") {
                        composable("HOME") {
                            HomeScreen(navController, listViewModel)
                        }
                        composable("EDIT") {
                            ListEditing(navController, listViewModel)
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.launch {
            saveDataAndFinish()
            Log.d("Saved onPause", "Save")
        }
    }

    private suspend fun saveDataAndFinish() {
        val appState = AppData(
            list = listViewModel.list.value
        )
        saveAppData(appState, dataStore)
        Log.d("Saved", "Data saved before finish")
        finish()
    }


}

