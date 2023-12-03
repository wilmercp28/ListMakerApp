package listmakerapp.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import listmakerapp.main.data.getAppData
import listmakerapp.main.ui.theme.ListMakerAppTheme

class MainActivity : ComponentActivity() {

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "AppState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            val appData = getAppData(dataStore).first()

        }


        setContent {
            val navController = rememberNavController()
            ListMakerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController, startDestination = "HOME") {

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


        Log.d("Saved", "Data saved before finish")
        finish()
    }


}

