package com.example.filmapp.Configuracoes

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import com.example.filmapp.R
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_seguranca.*
import kotlinx.android.synthetic.main.fragment_seguranca.view.*


class SegurancaFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_seguranca, container, false)

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser


        println("aquiii"+Constants.loginGoogle)
        if (user != null) {
            if(user.providerData[1].providerId == "google.com" || user.providerData[0].providerId == "google.com"){
                view.btnAtualizar.visibility = View.GONE
                view.novaSenha.visibility = View.GONE
                view.confimarSenha.visibility = View.GONE
                view.viewNovaSenha.visibility = View.GONE
                view.viewConfirmarSenha.visibility = View.GONE
                view.btnDesativarConta.setOnClickListener {

                    user.delete().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Conta deletada!", Toast.LENGTH_LONG).show()
                            val intent = Intent(context, ConfiguracoesActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            intent.putExtra("LOGOUT", true)
                            startActivity(intent)
                            activity?.finish()
                        } else
                            Toast.makeText(context, "Erro ao deletar conta!", Toast.LENGTH_LONG)
                                .show()
                    }
                }



            } else if (!Constants.loginGoogle && !isLoggedIn()) {

                view.btnDesativarConta.setOnClickListener {

                    user.delete().addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            Toast.makeText(context, "Conta deletada!", Toast.LENGTH_LONG).show()
                            val intent = Intent(context, ConfiguracoesActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            intent.putExtra("LOGOUT", true)
                            startActivity(intent)
                            activity?.finish()
                        } else
                            Toast.makeText(context, "Erro ao deletar conta!", Toast.LENGTH_LONG).show()
                    }
                }

                //            Firebase.auth.currentUser?.delete()


                //            val intent = Intent(context , LoginActivity::class.java)
                //            startActivity(intent)

                //            LoginManager.getInstance().logOut()





            } else if(isLoggedIn()){
                view.btnAtualizar.visibility = View.GONE
                view.novaSenha.visibility = View.GONE
                view.confimarSenha.visibility = View.GONE
                view.viewNovaSenha.visibility = View.GONE
                view.viewConfirmarSenha.visibility = View.GONE

                view.btnDesativarConta.setOnClickListener {
                    LoginManager.getInstance().logOut()
                    user.delete().addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            Toast.makeText(context, "Conta deletada!", Toast.LENGTH_LONG).show()
                            val intent = Intent(context, ConfiguracoesActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            intent.putExtra("LOGOUT", true)
                            startActivity(intent)
                            activity?.finish()
                        } else
                            Toast.makeText(context, "Erro ao deletar conta!", Toast.LENGTH_LONG).show()
                    }}


            } } else {
            view.btnAtualizar.visibility = View.GONE
            view.novaSenha.visibility = View.GONE
            view.confimarSenha.visibility = View.GONE
            view.viewNovaSenha.visibility = View.GONE
            view.viewConfirmarSenha.visibility = View.GONE
        }

        view.btnAtualizar.setOnClickListener{
            if(novaSenha.text.toString() == confimarSenha.text.toString()){
                var newPwd = novaSenha.text.toString()
                if (user != null) {


                    user.updatePassword(novaSenha.text.toString()).addOnCompleteListener { task ->
                        if(task.isSuccessful)
                            Toast.makeText(context, "Senha alterada!", Toast.LENGTH_LONG).show()
                        else
                            Toast.makeText(context, "Erro ao alterar senha!", Toast.LENGTH_LONG).show()
                    }
                }
            }
//            var resetPassword = EditText(context)
//            var passReset: AlertDialog.Builder = AlertDialog.Builder(context)
//            passReset.setTitle("Resetar senha?")
//            passReset.setMessage("Coloque uma nova senha:")
//            passReset.setView(resetPassword)
//            passReset.setPositiveButton("Sim", DialogInterface.OnClickListener { dialog, which ->
//                var newPsw: String = resetPassword.text.toString()
//                user?.updatePassword(newPsw)?.addOnSuccessListener {
//                    Toast.makeText(context, "Senha alterada!", Toast.LENGTH_LONG).show()
//                }?.addOnFailureListener {
//                    Toast.makeText(context, "Erro ao alterar senha!", Toast.LENGTH_LONG).show()
//                }
//
//            })


        }

//        view.btnAtualizarEmail.setOnClickListener{
//            updateEmail()
//        }

        return view



    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun updatePwd(){


    }

//    fun updateEmail(){
//        var user = Firebase.auth.currentUser
//        if(novoEmail.text.toString() == confirmarEmail.text.toString()) {
//            var newEmail = novoEmail.text.toString()
//            if (user != null) {
//                user.updateEmail(newEmail)
//                Toast.makeText(context, "Email alterada!", Toast.LENGTH_LONG).show()
//
//            }
//
//            } else{
//            Toast.makeText(context, "Erro! Digite novamente.", Toast.LENGTH_LONG).show()
//        }
//
//    }


    fun isLoggedIn(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired



        return isLoggedIn
    }



}

