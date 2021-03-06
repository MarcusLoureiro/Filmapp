package com.example.filmapp.Series.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.filmapp.Configuracoes.ConfiguracoesActivity
import com.example.filmapp.Entities.TV.Season
import com.example.filmapp.Entities.TV.TvDetails
import com.example.filmapp.Media.Adapters.ViewPagerMedia
import com.example.filmapp.R
import com.example.filmapp.Series.Fragments.*
import com.example.filmapp.home.descubra.DescubraActivity
import kotlinx.android.synthetic.main.activity_serie_temporada_selected.*


open class SerieTemporadaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_serie_temporada_selected)

        setSupportActionBar(toolbarTemporada)

        toolbarTemporada.setNavigationOnClickListener {
            finish()
        }
        setUpTabs()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    //Usado pra add ações de click aos itens do Menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.descubra_toolbarMenu -> {
                callDescubraPage()
                true
            }

            R.id.configurações_toolbarMenu -> {
                callConfiguracoesPage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun callDescubraPage(){
        val intent = Intent(this, DescubraActivity::class.java)
        startActivity(intent)
    }

    fun callConfiguracoesPage(){
        val intent = Intent(this, ConfiguracoesActivity::class.java)
        startActivity(intent)
    }


    fun setUpTabs() {
        var temp = ""
        val serie = intent.getSerializableExtra("serie") as? TvDetails
        val season = intent.getSerializableExtra("season") as? Season
        val poster = intent.getSerializableExtra("poster_season") as? String
        Log.i("sinopse season", season!!.overview)
        val Temporada = TemporadaFragment.newInstance(season, poster)
        val Episodios = EpisodiosFragment.newInstance(serie, season)
        val adapter = ViewPagerMedia(supportFragmentManager)
        if(season?.season_number == 0){
             temp = "Especial"
        }else{
            temp = season?.season_number.toString()
        }
        adapter.addFragment(Temporada, "${serie?.name} - Temporada ${temp}")
        adapter.addFragment(Episodios, "Episódios")
        viewPagerSeriesTemporada.adapter = adapter
        tabsSeriesTemporada.setupWithViewPager(viewPagerSeriesTemporada)
    }


}





