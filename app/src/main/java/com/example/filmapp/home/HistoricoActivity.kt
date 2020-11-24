package com.example.filmapp.Home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filmapp.Classes.Media
import com.example.filmapp.Configuracaoes.ConfiguracoesActivity
import com.example.filmapp.Home.Adapters.RecyclerViews.HistoricoAdapter
import com.example.filmapp.R
import com.example.filmapp.Series.Ui.SerieSelectedActivity
import kotlinx.android.synthetic.main.activity_historico.*

class HistoricoActivity : AppCompatActivity(), HistoricoAdapter.onHistoricoItemClickListener {
    private val mediaList = getMediaList()
    private val adapter = HistoricoAdapter(mediaList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historico)

        //Iniciando o ReciclerView Histórico
        rv_historicoList.adapter = adapter
        rv_historicoList.layoutManager = LinearLayoutManager(this)
        rv_historicoList.isHorizontalFadingEdgeEnabled
        rv_historicoList.setHasFixedSize(true)

        setSupportActionBar(toolbarHistoricoPage)

        toolbarHistoricoPage.setNavigationOnClickListener {
            callHome()
        }
    }

    //Usado para add o Menu a Toolbar
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

    fun callHome(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    fun getMediaList(): ArrayList<Media>{
        return arrayListOf<Media>(
            Media(1,R.drawable.academy_image01,"The Umbrella Academy", "Série", "2x08 - O Que Eu Sei", "21/08/12", "Netflix", "4 Temporadas", "37 Episodeos"),
            Media(1,R.drawable.fear_image01,"The Fear Walking Dead", "Filme", "", "08/08/12", "Amazon", "8 Temporadas", "2 Episodeos"),
            Media(1,R.drawable.flash_image01,"The Flash", "Série", "2x08 - O Que Eu Sei", "21/09/12", "Netflix", "3 Temporadas", "7 Episodeos"),
            Media(1,R.drawable.the_boys_image01,"The Boys", "Filme", "", "21/08/18", "Amazon", "7 Temporadas", "87 Episodeos"),
            Media(1,R.drawable.grey_image01,"Grey's Anatomy", "Série", "2x08 - O Que Eu Sei", "75/08/12", "Netflix", "1 Temporadas", "10 Episodeos")
        )
    }

    fun callDescubraPage(){
        val intent = Intent(this, DescubraActivity::class.java)
        startActivity(intent)
    }

    fun callConfiguracoesPage(){
        val intent = Intent(this, ConfiguracoesActivity::class.java)
        startActivity(intent)
    }

    override fun historicoItemClick(position: Int) {
        val media = mediaList.get(position)

        val intent = Intent(this, SerieSelectedActivity::class.java)
        startActivity(intent)
    }

}