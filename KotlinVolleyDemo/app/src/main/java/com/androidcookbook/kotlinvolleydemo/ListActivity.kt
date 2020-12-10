package com.androidcookbook.kotlinvolleydemo

import android.app.Activity
import android.app.ListActivity
import android.os.Bundle
import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * The Main (List) Activity, in Kotlin
 */
class ListActivity : Activity() {

    final var url = "https://androidcookbook.com/seam/resource/rest/recipe/list";
    final var TAG = "VolleyDemoKotlin.ListActivity"
    var listView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "ListActivity.onCreate()")
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.listview) as RecyclerView
        if (isNetworkConnected()) {
            getRecipes()
        }
    }

    private fun getRecipes() {
        Log.d(TAG, "ListActivity.getRecipes()")
        val queue = Volley.newRequestQueue(this)

        val jsonArrayRequest =  JsonArrayRequest(Request.Method.GET, url,
                JSONArray(),
                Response.Listener<JSONArray> { response ->
                    try {
                        process(response)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { })
        queue.add(jsonArrayRequest)
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun process(response: JSONArray) {
        Log.d(TAG, "ListActivity.process()")
        for (i in 1..response.length()) {
            Log.d(TAG, "${i} ==> ${response.get(i)}")
        }
    }
}
