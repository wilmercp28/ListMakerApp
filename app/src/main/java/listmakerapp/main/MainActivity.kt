package listmakerapp.main

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
import kotlinx.coroutines.launch
import listmakerapp.main.ui.composablesScreens.NavHostController
import listmakerapp.main.ui.theme.ListMakerAppTheme
import listmakerapp.main.viewModels.ShareViewModel

class MainActivity : ComponentActivity() {

    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "AppState")
    private val shareViewModel: ShareViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        lifecycleScope.launch {
            shareViewModel.changeLoadingState()
        }
        setContent {
            ListMakerAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHostController()
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

    private fun saveDataAndFinish() {


        Log.d("Saved", "Data saved before finish")
        finish()
    }


}

