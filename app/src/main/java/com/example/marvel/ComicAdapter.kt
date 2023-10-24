package com.example.marvel
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ComicAdapter(private val comicList: MutableList<Comic>) :
    RecyclerView.Adapter<ComicAdapter.ComicViewHolder>() {

    inner class ComicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val characterImageView: ImageView = itemView.findViewById(R.id.characterImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.comic_item, parent, false)
        return ComicViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        val currentComic = comicList[position]
        holder.titleTextView.text = currentComic.title
        holder.descriptionTextView.text = currentComic.description

        // Load and display the character image using Glide
        Glide.with(holder.characterImageView.context)
            .load(currentComic.characterImageUrl)
            .into(holder.characterImageView)
    }

    override fun getItemCount(): Int {
        return comicList.size
    }
        fun updateComicList(newComicList: List<Comic>) {
         comicList.clear()
         comicList.addAll(newComicList)
         notifyDataSetChanged()
            Log.d("ComicAdapter", "Updated comicList with new items: $newComicList")
        }
    }


