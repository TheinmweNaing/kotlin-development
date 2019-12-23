package com.team.attendancekt

import android.content.Context
import androidx.room.Room
import com.team.attendancekt.model.AppDatabase
import com.team.attendancekt.model.repo.MemberRepo

interface ServiceLocator {

    companion object {
        lateinit var instance: ServiceLocator
        fun getInstance(context: Context) = if (!this::instance.isInitialized) {
            instance = DefaultServiceLocator(context)
            instance
        } else {
            instance
        }
    }

    fun memberRepo(): MemberRepo

    class DefaultServiceLocator(private val context: Context) : ServiceLocator {
        val database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()

        override fun memberRepo() = MemberRepo(database.memberDao())

    }

}