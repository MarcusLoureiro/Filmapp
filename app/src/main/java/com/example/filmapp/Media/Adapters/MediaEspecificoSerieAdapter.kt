package com.example.filmapp.Media.Adapters


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmapp.Entities.APIConfig.Config
import com.example.filmapp.Entities.APIConfig.URL_IMAGE
import com.example.filmapp.Entities.TV.TvDetails
import com.example.filmapp.Media.UI.MediaSelectedActivity
import com.example.filmapp.R
import com.example.filmapp.Series.Ui.SerieTemporadaActivity
import com.squareup.picasso.Picasso


class MediaEspecificoSerieAdapter(
    private var listMediaEspecifico: TvDetails,
    val listener: OnMediaSerieClickListener,
    val config: Config
) : RecyclerView.Adapter<MediaEspecificoSerieAdapter.MediasViewHolder>() {
    val picasso = Picasso.get()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MediasViewHolder {
        var itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_media_especifica, parent, false)
        return MediasViewHolder(itemView)
    }

    override fun getItemCount() = listMediaEspecifico.seasons.size


    override fun onBindViewHolder(holder: MediasViewHolder, position: Int) {
        val serie = listMediaEspecifico
        val season = listMediaEspecifico.seasons[position]

        if(season.poster_path != "" && season.poster_path != null){
            picasso.load(URL_IMAGE + season.poster_path).into(holder.imgMediaEspecica)
        }else{
            picasso.load(R.drawable.sem_imagem).into(holder.imgMediaEspecica)
        }
        if(season.season_number != 0 && season.poster_path != null){
            holder.tvMediaEspecifica.text = "Temporada ${season.season_number}"
        }else{
            holder.tvMediaEspecifica.text = "Temporadas Indisponíveis"
        }

    }

    interface OnMediaSerieClickListener {
        fun SeriemediaClick(position: Int)

    }

    inner class MediasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val tvMediaEspecifica: TextView = itemView.findViewById(R.id.tv_mediaEspecifica)
        val imgMediaEspecica: ImageView = itemView.findViewById(R.id.img_MediaEspecifica)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (RecyclerView.NO_POSITION != position) {
                listener.SeriemediaClick(position)
            }
        }
    }
}