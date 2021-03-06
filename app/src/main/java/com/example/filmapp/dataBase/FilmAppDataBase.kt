package com.example.filmapp.dataBase

import android.content.Context
import androidx.room.*
import com.example.filmapp.Media.dataBase.FavoritosDAO
import com.example.filmapp.Media.dataBase.FavoritosEntity
import com.example.filmapp.home.agenda.dataBase.AssistirMaisTardeDAO
import com.example.filmapp.home.agenda.dataBase.AssistirMaisTardeEntity
import com.example.filmapp.home.historico.dataBase.DateTypeConverters
import com.example.filmapp.home.historico.dataBase.HistoricoDAO
import com.example.filmapp.home.historico.dataBase.HistoricoEntity

@Database(
    entities = [AssistirMaisTardeEntity::class, HistoricoEntity::class, FavoritosEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FilmAppDataBase : RoomDatabase() {

    abstract fun assistirMaisTardeDao(): AssistirMaisTardeDAO
    abstract fun historicoDao(): HistoricoDAO
    abstract fun favoritosDAO(): FavoritosDAO

    companion object {
        @Volatile
        private var INSTANCE: FilmAppDataBase? = null

        fun getDataBase(context: Context): FilmAppDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FilmAppDataBase::class.java,
                    "filmAppDataBase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}