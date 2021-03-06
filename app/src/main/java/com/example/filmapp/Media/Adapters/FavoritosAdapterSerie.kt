package com.example.filmapp.Media.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmapp.Entities.APIConfig.URL_IMAGE
import com.example.filmapp.Media.UI.MediaSelectedActivity
import com.example.filmapp.Media.dataBase.FavoritoScope
import com.example.filmapp.Media.dataBase.FavoritosEntity
import com.example.filmapp.R
import com.squareup.picasso.Picasso

class FavoritosAdapterSerie(val listener: FavoritosItemClickListener) :
    RecyclerView.Adapter<FavoritosAdapterSerie.FavoritosViewHolder>() {

    var mediaList = listOf<FavoritoScope>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoritosViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_poster, parent, false)
        return FavoritosViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavoritosViewHolder, position: Int) {
        val currentItem: FavoritoScope = mediaList[position]
        holder.mediaName.text = currentItem.title
        Picasso.get().load(URL_IMAGE + currentItem.poster_path).into(holder.mediaImage)
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    interface FavoritosItemClickListener {
        fun favoritosItemClickSerie(position: Int)
        fun favoritosLongClickSerie(position: Int)
    }

    inner class FavoritosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {
        val mediaName: TextView = itemView.findViewById(R.id.mediaName)
        val mediaImage: ImageView = itemView.findViewById(R.id.mediaImage)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (RecyclerView.NO_POSITION != position) {
                listener.favoritosItemClickSerie(position)
            }
        }
        override fun onLongClick(v: View?): Boolean {
            val position = adapterPosition
            if (RecyclerView.NO_POSITION != position) {
                listener.favoritosLongClickSerie(position)
                return true
            }
            return false
        }
    }

    fun addList(list: List<FavoritoScope>) {
        mediaList = list
        notifyDataSetChanged()
    }

}