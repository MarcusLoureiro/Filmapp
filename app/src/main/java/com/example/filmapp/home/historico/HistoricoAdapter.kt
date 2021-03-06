package com.example.filmapp.home.historico

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.filmapp.Entities.TV.ResultTv
import com.example.filmapp.R
import com.example.filmapp.home.historico.dataBase.HistoricoEntity
import com.example.filmapp.home.historico.realtimeDatabase.HistoricoScope
import com.squareup.picasso.Picasso

class HistoricoAdapter(val listener: onHistoricoItemClickListener) :
    RecyclerView.Adapter<HistoricoAdapter.HistoricoViewHolder>() {

    var mediaList = arrayListOf<HistoricoScope>()
    var isClickable = true

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoricoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_historicolist, parent, false)
        return HistoricoViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: HistoricoViewHolder,
        position: Int
    ) {
        val currentItem: HistoricoScope = mediaList[position]

        var url = "https://image.tmdb.org/t/p/w500" + currentItem.poster_path
        Picasso.get().load(url).into(holder.mediaImage)

        holder.mediaName.setText(currentItem.formattedTitle)

        var seasonNumber = currentItem.seasonNumber.toString()
        var episodeNumber = currentItem.episodeNumber.toString()
        var episodeName = currentItem.formattedEpisodeTitle

        if(currentItem.type == "Tv"){

            if(episodeName == ("Episódio " + episodeNumber))
                holder.serieEpisodeOurType.setText("Episódio " + episodeNumber)
            else if(episodeName.length != 0)
                holder.serieEpisodeOurType.setText("Episódio " + episodeNumber + " - " + episodeName)
            else
                holder.serieEpisodeOurType.setText("Episódio " + episodeNumber)

            holder.serieSeason.setText(seasonNumber + "º Temporada")
        }else{
            holder.serieEpisodeOurType.setText("Filme")
            holder.serieSeason.setText(" ")
            holder.mediaPreviewDate.visibility = View.INVISIBLE
        }

        if(currentItem.date.length > 5)
            holder.mediaPreviewDate.setText(currentItem.date)
        else
            holder.mediaPreviewDate.visibility = View.INVISIBLE

    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    interface onHistoricoItemClickListener {
        fun historicoItemClick(position: Int)
        fun historicoItemLongClick(position: Int)
    }

    inner class HistoricoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {
        val mediaName: TextView = itemView.findViewById(R.id.tv_mediaName_historicoItem)
        val serieEpisodeOurType: TextView = itemView.findViewById(R.id.tv_serieEpisodeOurType_historicoItem)
        val serieSeason: TextView = itemView.findViewById(R.id.tv_serieSeason_historicoItem)
        val mediaImage: ImageView = itemView.findViewById(R.id.iv_mediaImage_historicoItem)
        val mediaPreviewDate: TextView = itemView.findViewById(R.id.tv_mediaPreviewDate_historicoItem)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            if(isClickable == true) {
                val position = adapterPosition
                if (RecyclerView.NO_POSITION != position) {
                    listener.historicoItemClick(position)
                }
            }
        }

        override fun onLongClick(v: View?): Boolean {
            val position = adapterPosition
            if (RecyclerView.NO_POSITION != position) {
                listener.historicoItemLongClick(position)
                return true
            }
            return false
        }
    }

    fun addList(list: ArrayList<HistoricoScope>) {
        mediaList = list
        notifyDataSetChanged()
    }
}