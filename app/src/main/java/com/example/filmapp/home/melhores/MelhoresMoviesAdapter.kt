package com.example.filmapp.home.melhores

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.filmapp.Entities.Movie.ResultMovie
import com.example.filmapp.R
import com.example.filmapp.dataBase.AssistirMaisTardeRepository
import com.example.filmapp.dataBase.FilmAppDataBase
import com.example.filmapp.home.agenda.dataBase.AssistirMaisTardeDAO
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MelhoresMoviesAdapter(val listener: onMelhoresMovieClickListener) :
    RecyclerView.Adapter<MelhoresMoviesAdapter.MelhoresListsViewHolder>() {

    var mediaList = arrayListOf<ResultMovie>()
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MelhoresListsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_medialist, parent, false)
        return MelhoresListsViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: MelhoresListsViewHolder,
        position: Int
    ) {
        val currentItem: ResultMovie = mediaList[position]

        holder.mediaName.text = currentItem.formattedTitle
        holder.mediaFirstAirDate.text = currentItem.release_date
        holder.ratingStarsNumber.rating = currentItem.numberStars.toFloat()
        holder.mediaEvaluation.text = currentItem.vote_average.toString() + "/5"
        holder.mediaPosition.text = (position + 1).toString() + ". "

        var url = "https://image.tmdb.org/t/p/w500" + currentItem.poster_path
        Picasso.get().load(url).into(holder.mediaImage)

        //Aq verifica se o filme já foi add a lista de Assistir Mais Tarde ou não
        if(currentItem.assistirMaisTardeIndication == true){
            holder.assistirMaisTardeIndication.setImageResource(R.drawable.ic_assistir_mais_tarde_roxo)
        }else{
            holder.assistirMaisTardeIndication.setImageResource(R.drawable.ic_assistir_mais_tarde)
        }


        holder.assistirMaisTardeIndication.setOnClickListener {
            if(currentItem.assistirMaisTardeIndication == false){
                holder.assistirMaisTardeIndication.setImageResource(R.drawable.ic_assistir_mais_tarde_roxo)
                listener.saveInAssistirMaisTardeList(position)
                currentItem.assistirMaisTardeIndication = true
            }else{
                holder.assistirMaisTardeIndication.setImageResource(R.drawable.ic_assistir_mais_tarde)
                listener.removeOfAssistirMaisTardeList(position)
                currentItem.assistirMaisTardeIndication = false
            }
        }

        //Aq verifica se o filme já foi assistido pelo usuário ou não
        if(currentItem.watched == true){
            holder.watchedIndication.setImageResource(R.drawable.ic_check_box_roxo)
        }else{
            holder.watchedIndication.setImageResource(R.drawable.ic_check_box)
        }


        holder.watchedIndication.setOnClickListener {
            if(currentItem.watched == false){
                holder.watchedIndication.setImageResource(R.drawable.ic_check_box_roxo)
                listener.saveInHistorico(position)
                currentItem.watched = true
            }else{
                holder.watchedIndication.setImageResource(R.drawable.ic_check_box)
                listener.removeOfHistorico(position)
                currentItem.watched = false
            }
        }

        holder.shareIndication.setOnClickListener {
            holder.shareIndication.setImageResource(R.drawable.ic_compartilhar_roxo)
            listener.share(position)
            scope.launch {
                delay(2000)
                holder.shareIndication.setImageResource(R.drawable.ic_compartilhar)
            }
        }
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    interface onMelhoresMovieClickListener {
        fun melhoresItemClick(position: Int)
        fun saveInAssistirMaisTardeList(position: Int)
        fun removeOfAssistirMaisTardeList(position: Int)
        fun saveInHistorico(position: Int)
        fun removeOfHistorico(position: Int)
        fun share(position: Int)
    }

    inner class MelhoresListsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val mediaName: TextView = itemView.findViewById(R.id.tv_mediaName_medialist)
        val mediaImage: ImageView = itemView.findViewById(R.id.iv_mediaImage_medialist)
        val mediaFirstAirDate: TextView = itemView.findViewById(R.id.tv_mediaFirstAirDate_medialist)
        val ratingStarsNumber: RatingBar = itemView.findViewById(R.id.ratingBar_mediaItem)
        val mediaEvaluation: TextView = itemView.findViewById(R.id.tv_evaluation_medialist)
        val mediaPosition: TextView = itemView.findViewById(R.id.tv_mediaPosition_medialist)

        val assistirMaisTardeIndication: ImageView = itemView.findViewById(R.id.assistirMaisTardeIndication_medialist)
        val watchedIndication: ImageView = itemView.findViewById(R.id.followingStatusIndication_medialist)
        val shareIndication: ImageView = itemView.findViewById(R.id.shareIndication_medialist)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (RecyclerView.NO_POSITION != position) {
                listener.melhoresItemClick(position)
            }
        }

    }

    fun addList(list: ArrayList<ResultMovie>) {
        mediaList.addAll(list)
        notifyDataSetChanged()
    }
}
