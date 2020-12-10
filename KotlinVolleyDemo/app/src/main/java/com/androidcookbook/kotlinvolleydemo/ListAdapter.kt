package com.androidcookbook.kotlinvolleydemo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*

/**
 * The List Adapter, in Kotlin.
 */
class ListViewAdapter: RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    private var recipes: List<Recipe> = ArrayList<Recipe>()

    constructor(recipes: List<Recipe>) {
        this.recipes = recipes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mRecipe = recipes[position]
        holder.mRecipeName.text = recipes[position].title
        holder.mRecipeProblem.text = recipes[position].problem
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mRecipe: Recipe? = null
        val mRecipeName: TextView
        val mRecipeProblem: TextView

        init {
            mRecipeName = mView.findViewById(R.id.title) as TextView
            mRecipeProblem = mView.findViewById(R.id.problem) as TextView
        }

        override fun toString(): String {
            return super.toString() + " '" + mRecipeName.text + "'"
        }
    }
}
