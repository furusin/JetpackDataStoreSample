package net.furusin.www.jetpackdatastoresample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var dataStore: DataStore<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataStore = createDataStore(fileName = "preferences", serializer = UserSerializer)

        findViewById<Button>(R.id.saveAgeButton).setOnClickListener {
            GlobalScope.launch {
                setUserAge(20)
            }
        }

        findViewById<Button>(R.id.saveNameButton).setOnClickListener {
            GlobalScope.launch {
                setUserName("furusin")
            }
        }

        findViewById<Button>(R.id.loadButton).setOnClickListener {
            GlobalScope.launch {
                getStoredUser().collect { Log.d("test", "age = ${it.age}, name = ${it.name}") }
            }
        }
    }

    private suspend fun setUserAge(age: Int) {
        dataStore.updateData { currentUser: User ->
            currentUser.toBuilder()
                .setAge(age)
                .build()
        }
    }

    private suspend fun setUserName(name: String) {
        dataStore.updateData { currentUser: User ->
            currentUser.toBuilder()
                .setName(name)
                .build()
        }
    }

    private suspend fun storeUser(user: User) {
        dataStore.updateData { currentUser ->
            currentUser.toBuilder()
                .setAge(user.age)
                .setName(user.name)
                .build()
        }
    }

    private fun getStoredUser(): Flow<User> = dataStore.data
}