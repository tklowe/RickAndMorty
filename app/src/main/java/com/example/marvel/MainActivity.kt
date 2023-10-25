package com.example.marvel

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import okhttp3.*

class MainActivity : AppCompatActivity() {
    var characterList = mutableListOf<Character>()
    val apiEndpoint = "https://rickandmortyapi.com/api/character"
    var recyclerView: RecyclerView? = null
    // Your other variables and setup code remain the same.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this)

        val characterAdapter = CharacterAdapter(characterList)
        recyclerView?.adapter = characterAdapter

        // Create an EndlessRecyclerViewScrollListener
        val scrollListener = object : EndlessRecyclerViewScrollListener(recyclerView?.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Load more characters when the user scrolls to the end
                loadMoreCharacters(page)
            }
        }

        recyclerView?.addOnScrollListener(scrollListener)

        loadMoreCharacters(1) // Initial load (assuming you start from page 1)
    }

    private fun loadMoreCharacters(page: Int) {
        val url = "$apiEndpoint?page=$page"

        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.d("Rick and Morty API", "Request successful")

                val results = json.jsonObject.getJSONArray("results")
                val newCharacterList = mutableListOf<Character>()

                for (i in 0 until results.length()) {
                    val characterObj = results.getJSONObject(i)

                    val character = Character(
                        characterObj.getString("name"),
                        characterObj.getString("species"),
                        characterObj.getString("image")
                    )

                    newCharacterList.add(character)
                }

                characterList.addAll(newCharacterList)
                recyclerView?.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e("Rick and Morty API", "Request failed: ${throwable?.message ?: "Unknown Error"}")
            }
        })
    }
}
