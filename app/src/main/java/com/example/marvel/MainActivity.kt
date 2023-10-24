package com.example.marvel

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    var comicList = mutableListOf<Comic>()
    val apiKey = "4c8bf2be985dfdfb2cfd98f8c2579b55" // Replace with your Marvel API key
    val apiEndpoint = "http://gateway.marvel.com/v1/public/comics"

    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this)

        val comicAdapter = ComicAdapter(comicList)
        recyclerView?.adapter = comicAdapter

        // Create an EndlessRecyclerViewScrollListener
        val scrollListener = object : EndlessRecyclerViewScrollListener(recyclerView?.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Load more comics when the user scrolls to the end
                loadMoreComics(page)
            }
        }

        recyclerView?.addOnScrollListener(scrollListener)

        loadMoreComics(0) // Initial load
    }

    private fun loadMoreComics(page: Int) {
        val offset = page * 20 // Adjust as needed
        val ts = "1" // You can change the timestamp value as needed
        val hash = "YOUR_HASH" // Calculate the hash as required

        val url = "$apiEndpoint?ts=$ts&apikey=$apiKey&hash=$hash&offset=$offset"

        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.d("Marvel Comics API", "Request successful")

                val data = json.jsonObject.getJSONObject("data")
                val results = data.getJSONArray("results")
                val newComicList = mutableListOf<Comic>()

                for (i in 0 until results.length()) {
                    val comicObj = results.getJSONObject(i)

                    val comic = Comic(
                        comicObj.getInt("id"),
                        comicObj.getString("title"),
                        comicObj.getString("description"),
                        comicObj.getJSONObject("thumbnail").getString("path") +
                                "." + comicObj.getJSONObject("thumbnail").getString("extension")
                    )

                    newComicList.add(comic)
                }
            }
            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e("Marvel Comics API", "Request failed: ${throwable?.message ?: "Unknown Error"}")
            }
        })
    }
}