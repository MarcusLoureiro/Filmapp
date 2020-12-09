package com.example.filmapp.Media.Models

import com.example.filmapp.Services.Service



import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.Entities.APIConfig.API_KEY
import com.example.filmapp.Entities.APIConfig.Config
import com.example.filmapp.Entities.Movie.BaseMovie
import com.example.filmapp.Entities.Movie.SimilarMovies
import com.example.filmapp.Entities.TV.BaseTv
import com.example.filmapp.Entities.TV.TvDetails
import kotlinx.coroutines.launch

class EspecificoFragmentViewModel(val service: Service) : ViewModel() {


    var listDetails = MutableLiveData<TvDetails>()
    var listSimilar = MutableLiveData<SimilarMovies>()
    var config = MutableLiveData<Config>()


    fun getDetailsSerie(id: String){
        viewModelScope.launch {
            listDetails.value = service.getDetailsSerie(
                id,
                API_KEY,
                "en-US",
                1
            )
        }
    }
    fun getSimilarMovies(id: String){
        viewModelScope.launch {
            listSimilar.value = service.getSimilarMovies(
                id,
                API_KEY,
                "en-US",
                1
            )
        }
    }
    fun getConfig(){
        viewModelScope.launch {
            config.value = service.getApiConfig(
                "4a6baee1eff7d3911f03f59b9b8f43eb",
            )
        }
    }

}