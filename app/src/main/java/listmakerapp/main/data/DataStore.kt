package listmakerapp.main.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val APP_DATA_STORE_KEY = stringPreferencesKey("list_name")


fun DataStore<Preferences>.getAppData(): Flow<AppData> {
    return this.data.map { preferences ->
        val appDataJson = preferences[APP_DATA_STORE_KEY] ?: "{}"
        Gson().fromJson(appDataJson, AppData::class.java) ?: AppData(emptyList())
    }
}

suspend fun DataStore<Preferences>.saveAppData(appData: AppData) {
    val appDataJson = Gson().toJson(appData)
    this.edit { preferences ->
        preferences[APP_DATA_STORE_KEY] = appDataJson
    }
}
