package listmakerapp.main.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val APP_DATA_STORE_KEY = stringPreferencesKey("APP-State")



fun getAppData(dataStore: DataStore<Preferences>): Flow<AppData?> {
    return dataStore.data.map { preferences ->
        val appDataJson = preferences[APP_DATA_STORE_KEY] ?: "{}"
        Gson().fromJson(appDataJson, AppData::class.java)
    }
}

suspend fun saveAppData(appData: AppData, dataStore: DataStore<Preferences>) {
    val appDataJson = Gson().toJson(appData)
    dataStore.edit { preferences ->
        preferences[APP_DATA_STORE_KEY] = appDataJson
    }
}




