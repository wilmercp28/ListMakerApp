package listmakerapp.main

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import listmakerapp.main.data.AppData
import listmakerapp.main.data.getAppData
import listmakerapp.main.data.saveAppData
import listmakerapp.main.ui.composablesScreens.NavHostController
import listmakerapp.main.ui.theme.ListMakerAppTheme
import listmakerapp.main.viewModels.ShareViewModel

class MainActivity : ComponentActivity() {




    private val shareViewModel: ShareViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val dataStoreClass =  (application as DataStoreClass).dataStore
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            val appData = getAppData(dataStoreClass).first()
            if (appData != null) {
                shareViewModel.loadStates(appData)
                shareViewModel.changeLoadingState()
            }
        }

        setContent {
            ListMakerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHostController(shareViewModel)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            saveDataAndFinish()
        }
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.launch {
            saveDataAndFinish()
        }

    }

    private suspend fun saveDataAndFinish() {
        val dataStoreClass =  (application as DataStoreClass).dataStore
        val listState = shareViewModel.listState.value
        val appData = AppData(
            list = listState,
            selectedIndex = shareViewModel.selectedIndex.value,
            isShoppingMode = shareViewModel.isShoppingMode.value
        )
        saveAppData(appData, dataStoreClass)
        finish()
    }
}

class DataStoreClass : Application() {
    val dataStore: DataStore<Preferences> by preferencesDataStore(name = "APP-STATE")
}

