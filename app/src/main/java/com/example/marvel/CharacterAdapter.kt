package com.example.marvel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
class CharacterAdapter(private val characterList: List<Character>) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comic_item, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characterList[position]
        holder.bind(character)
    }

    override fun getItemCount(): Int {
        return characterList.size
    }

    inner class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(character: Character) {
            val characterNameTextView = itemView.findViewById<TextView>(R.id.characterName)
            val characterSpeciesTextView = itemView.findViewById<TextView>(R.id.characterSpecies)
            val characterImageView = itemView.findViewById<ImageView>(R.id.characterImage)
            characterNameTextView.text = character.name
            characterSpeciesTextView.text = character.species
            Glide.with(itemView.context).load(character.image).into(characterImageView)
        }
    }
}