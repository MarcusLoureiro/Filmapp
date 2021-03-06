package com.example.filmapp.Media.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.filmapp.Entities.APIConfig.Config
import com.example.filmapp.Entities.Movie.ResultMovie
import com.example.filmapp.Entities.Movie.SimilarMovies
import com.example.filmapp.Entities.TV.ResultTv
import com.example.filmapp.Entities.TV.TvDetails
import com.example.filmapp.Media.Adapters.MediaEspecificoMovieAdapter
import com.example.filmapp.Media.Adapters.MediaEspecificoSerieAdapter
import com.example.filmapp.Media.Models.EspecificoFragmentViewModel
import com.example.filmapp.Media.UI.MediaSelectedActivity
import com.example.filmapp.R
import com.example.filmapp.Series.Ui.SerieTemporadaActivity
import com.example.filmapp.Services.MainViewModel
import com.example.filmapp.Services.service
import kotlinx.android.synthetic.main.fragment_series_seasons.view.*
import java.io.Serializable


class MediaEspecificoFragment() : Fragment(),
    MediaEspecificoSerieAdapter.OnMediaSerieClickListener,
    MediaEspecificoMovieAdapter.OnMediaMovieClickListener {
    private lateinit var SerieDetails: TvDetails
    private lateinit var listaSemelhantes: SimilarMovies
    lateinit var serieAdapter: MediaEspecificoSerieAdapter
    lateinit var movieAdapter: MediaEspecificoMovieAdapter
    var Movie: Boolean? = null
    var Id: String? = null
    var config = Config()


    private val viewModelEspecificoFragment by viewModels<EspecificoFragmentViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return EspecificoFragmentViewModel(service) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            Movie = arguments?.getBoolean(movie)
            Id = arguments?.getString(idMedia)
        }
    }

    companion object {
        private val movie = "movie"
        private val idMedia = "id"
        fun newInstance(Movie: Boolean, Id: String?): MediaEspecificoFragment {
            val fragment = MediaEspecificoFragment()
            val args = Bundle()
            args.putBoolean(movie, Movie)
            args.putString(idMedia, Id)
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_series_seasons, container, false)
        if (Movie == true) {
            viewModelEspecificoFragment.config.observe(viewLifecycleOwner) {
                config = it
            }
            viewModelEspecificoFragment.getConfig()
            viewModelEspecificoFragment.listSimilar.observe(viewLifecycleOwner) {
                listaSemelhantes = it
                var adapter = MediaEspecificoMovieAdapter(listaSemelhantes, this, Movie, config)
                view?.rv_temporada.adapter = adapter
                view?.rv_temporada.layoutManager = GridLayoutManager(activity, 2)
                view?.rv_temporada.setHasFixedSize(true)
            }
            viewModelEspecificoFragment.getSimilarMovies(Id!!)
        } else {
            viewModelEspecificoFragment.config.observe(viewLifecycleOwner) {
                config = it
            }
            viewModelEspecificoFragment.getConfig()
            viewModelEspecificoFragment.listDetails.observe(viewLifecycleOwner) {
                SerieDetails = it
                val adapter = MediaEspecificoSerieAdapter(SerieDetails, this, config)
                view?.rv_temporada.adapter = adapter
                view?.rv_temporada.layoutManager = GridLayoutManager(activity, 2)
                view?.rv_temporada.setHasFixedSize(true)
            }
            viewModelEspecificoFragment.getDetailsSerie(Id!!)
        }
        return view
    }

    override fun SeriemediaClick(position: Int) {
        val serie = SerieDetails
        val season = serie.seasons[position]
        val intent = Intent(context, SerieTemporadaActivity::class.java)
        intent.putExtra("serie", serie)
        intent.putExtra("season", season)
        intent.putExtra("poster_season", season.poster_path)
        startActivity(intent)
    }

    override fun MoviemediaClick(position: Int) {
        val movie = listaSemelhantes.results.get(position)
        val intent = Intent(context, MediaSelectedActivity::class.java)
        intent.putExtra("poster", movie.poster_path)
        intent.putExtra("movie", true)
        intent.putExtra("id", movie.id)
        startActivity(intent)
    }
}
