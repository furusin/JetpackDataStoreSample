package net.furusin.www.jetpackdatastoresample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataStore = this.createDataStore(name = "preferences")

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            GlobalScope.launch {
                saveName()
            }
        }

        findViewById<Button>(R.id.loadButton).setOnClickListener {
            GlobalScope.launch {
                getName().collect {
                    Log.d("test", "name = $it")
                }
            }
        }
    }

    private fun getName(): Flow<String> =
        dataStore.data.map { preferences ->
            preferences[PreferencesKeys.KEY_NAME] ?: "NO NAME"
        }

    private suspend fun saveName() {
        dataStore.edit { prefernces: MutablePreferences ->
            prefernces[PreferencesKeys.KEY_NAME] = "furusin"
        }
    }

    private object PreferencesKeys {
        val KEY_NAME = preferencesKey<String>("KEY_NAME")
    }
}