package com.example.filmapp.Series.Fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmapp.Entities.APIConfig.URL_IMAGE
import com.example.filmapp.Media.Models.EpisodioFragmentViewModel
import com.example.filmapp.R
import com.example.filmapp.Services.service
import com.example.filmapp.home.historico.HistoricoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.custom_alert.view.*
import kotlinx.android.synthetic.main.fragment_series_episodio.*
import kotlinx.android.synthetic.main.fragment_series_episodio.view.*
import kotlinx.android.synthetic.main.fragment_series_geral.imgCompart
import kotlinx.android.synthetic.main.fragment_series_geral.view.imgCompart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class EpisodioFragment : Fragment() {

    var watched = false

    private lateinit var viewModelVisto: HistoricoViewModel
    val scope = CoroutineScope(Dispatchers.Main)
    var selcomp: Boolean = false
    var selMais: Boolean = false
    val picasso = Picasso.get()
    var Poster: String? = null
    var Sinopse: String? = null
    var Type: String? = null
    var Title: String? = null
    var Id: String? = null
    var Img: String? = null
    var Logo: String? = null
    var HomePage: String? = null
    var Id_ep: String? = null
    var NumberEp: Int? = null
    var NumberSeason: Int? = null
    var EpisodeTitle: String? = null

    val viewModel by viewModels<EpisodioFragmentViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return EpisodioFragmentViewModel(service) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            Poster = arguments?.getString(poster)
            Sinopse = arguments?.getString(sinopse)
            Id = arguments?.getString(idMedia) //= id da Série
            Type = arguments?.getString(type)
            Img = arguments?.getString(img)
            Logo = arguments?.getString(logo)
            HomePage = arguments?.getString(homePage)
            Title = arguments?.getString(title) //= título da Série
            Id_ep = arguments?.getString(id_ep) //= id do Episódio
            NumberEp = arguments?.getInt(numberEp) //= número do Episódio
            NumberSeason = arguments?.getInt(numberSeason) //= número da Temporada
            EpisodeTitle = arguments?.getString(episodeTitle) //= título do Episódio
        }
    }

    companion object {
        private val sinopse = "sinopse"
        private val poster = "poster"
        private val idMedia = "id"
        private val logo = "logo"
        private val homePage = "home"
        private val img = "img"
        private val type = "type"
        private val title = "title"
        private val id_ep = "id_ep"
        private val numberEp = "number_episode"
        private val numberSeason = "number_season"
        private val episodeTitle = "episodeTitle"
        fun newInstance(
            Sinopse: String?,
            Poster: String?,
            Id: String?,
            Type: String?,
            Img: String?,
            Logo: String?,
            HomePage: String,
            Title: String?,
            Id_ep: String?,
            NumberEp: Int?,
            NumberSeason: Int?,
            EpisodeTitle: String?
        ): EpisodioFragment {
            val fragment = EpisodioFragment()
            val args = Bundle()
            args.putString(sinopse, Sinopse)
            args.putString(poster, Poster)
            args.putString(idMedia, Id)
            args.putString(type, Type)
            args.putString(img, Img)
            args.putString(logo, Logo)
            args.putString(homePage, HomePage)
            args.putString(title, Title)
            args.putString(id_ep, Id_ep)
            args.putInt(numberEp, NumberEp!!)
            args.putInt(numberSeason, NumberSeason!!)
            args.putString(episodeTitle, EpisodeTitle)
            fragment.arguments = args
            return fragment
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_series_episodio, container, false)

        viewModelVisto = ViewModelProvider(this).get(HistoricoViewModel::class.java)

        //Personalizando a UI

        if (EpisodeTitle != null)
            view.nameEp.text = EpisodeTitle!!
        else
            view.nameEp.visibility = View.GONE

        if (Img != null)
            picasso.load(URL_IMAGE + Img).into(view.imgEp)
        else
            view.imgEp.visibility = View.GONE

        if(Logo != null)
            picasso.load(URL_IMAGE + Logo).into(view.imgLogo)
        else
            view.imgLogo.setImageResource(R.drawable.ic_add_circle)

        if(Sinopse?.length != 0)
            view.sinopseEp.text = Sinopse
        else
            view.sinopseEp.text = "Não há sinopse disponível no momento."

        view.imgCompartField.setOnClickListener {
            AbrirCompartilhar(Title!!, homePage)

            if (selcomp == false) {
                view.imgCompart.setImageResource(R.drawable.ic_compartilhar_grande_roxo)
                view.textCompart.setTextColor(Color.parseColor("#9D14C7"))
                selcomp = true
                scope.launch {
                    delay(2000)
                    view.imgCompart?.setImageResource(R.drawable.ic_compartilhar_grande)
                    view.textCompart.setTextColor(Color.WHITE)
                    selcomp = false
                }
            }
        }

        view.maisField.setOnClickListener {
            AbrirSiteLogo()
        }

        //Acompanhando/Histórico CodeInicio---------------------------------------------------------

        viewModel.getAcompanhadoList()

        //Verifica se o usuário está acompanhando a série no qual o episódio pertence
        viewModel.returnAcompanhandoList.observe(viewLifecycleOwner) {
            var result = viewModel.checkSerieInList(Id!!.toInt(), it)

            //Caso o usuário esteja acompanhando a série, a opção de marcar o episódio como
            //assistido será liberada
            if (result == true) {
                view.imgVistoField.visibility = View.VISIBLE
                viewModel.getWatchedEpisodesList(Id!!.toInt())
            }
        }

        //Verifica se o usuário já assistiu esse episódio
        viewModel.returnWatchedEpisodesList.observe(viewLifecycleOwner) {
            var result = viewModel.checkIfWatchedEpisode(Id_ep!!.toInt(), it)

            //Controle da cor do indicador
            if (result == true) {
                view.imgVisto.setImageResource(R.drawable.ic_visto_grande_roxo)
                view.textVisto.text = "VISTO"
                view.textVisto.setTextColor(Color.parseColor("#9D14C7"))
                watched = true
            } else {
                view.imgVisto.setImageResource(R.drawable.ic_visto_grande)
                view.textVisto.text = "NÃO VISTO"
                view.textVisto.setTextColor(Color.WHITE)
                watched = false
            }
        }

        view.imgVistoField.setOnClickListener {
            if (watched == true) {
                viewModel.deleteWatchList(Id_ep!!.toInt(), Id!!.toInt())
                viewModel.deleteFromHistoricoList(Id_ep!!.toInt())
                watched = false
                view.imgVisto.setImageResource(R.drawable.ic_visto_grande)
                view.textVisto.text = "NÃO VISTO"
                view.textVisto.setTextColor(Color.WHITE)
            } else {
                viewModel.addWatchList(Id_ep!!.toInt(), Id!!.toInt())
                viewModel.saveInHistoricoList(
                    Id!!.toInt(),
                    Title.toString(),
                    Poster.toString(),
                    NumberSeason!!,
                    NumberEp!!,
                    EpisodeTitle.toString(),
                    Id_ep!!.toInt()
                )
                watched = true
                view.imgVisto.setImageResource(R.drawable.ic_visto_grande_roxo)
                view.textVisto.text = "VISTO"
                view.textVisto.setTextColor(Color.parseColor("#9D14C7"))
            }
        }

        //Acompanhando/Histórico CodeFinal----------------------------------------------------------

        return view
    }

    fun AbrirSiteLogo() {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(HomePage))
            Log.i("HOMEPAGE", HomePage.toString())
            startActivity(intent)
        }catch (e : Exception){
            Toast.makeText(activity, "Estamos com algum problema. Tente novamente mais tarde.", Toast.LENGTH_LONG).show()
        }
    }

    fun AbrirCompartilhar(title: String, link: String) {
        val ShareIntent = Intent().apply {
            this.action = Intent.ACTION_SEND
            this.putExtra(Intent.EXTRA_TEXT, "Já viu $title? Tá disponível aqui:$link")
            this.type = "text/plain"
        }
        startActivity(ShareIntent)
    }

//    fun creatAlertException(e: Exception) {
//        val user = FirebaseAuth.getInstance().currentUser
//        val builder = AlertDialog.Builder(requireActivity()).create()
//        val view: View =
//            LayoutInflater.from(requireActivity()).inflate(R.layout.custom_alert_erro, null)
//        builder.setView(view)
//        builder.show()
//        view.btAlert_confirm.setOnClickListener {
//            val firebaseDB =
//                FirebaseDatabase.getInstance().getReference().child("erros/${user?.uid}")
//                    .setValue(e.toString())
//            Toast.makeText(
//                activity,
//                "Erro reportado, desculpe-nos pelo transtorno",
//                Toast.LENGTH_SHORT
//            ).show()
//            builder.dismiss()
//            getActivity()?.finish();
//        }
//        view.btAlert_Notconfirm.setOnClickListener {
//            Toast.makeText(activity, "Erro ignorado", Toast.LENGTH_SHORT).show()
//            builder.dismiss()
//            getActivity()?.finish();
//
//        }

    }



