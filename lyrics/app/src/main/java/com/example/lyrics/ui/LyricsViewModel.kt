package com.example.lyrics.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.lyrics.model.RetrofitManager
import com.example.lyrics.model.api.LyricsAPI
import com.example.lyrics.model.service.LyricsService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LyricsViewModel(application: Application) : AndroidViewModel(application) {

    private val service: LyricsService
    private val compositeDisposable = CompositeDisposable()
    var lyrics = MutableLiveData<String>()
    var result = MutableLiveData<Boolean>()

    init {
        service = LyricsService(RetrofitManager.create(LyricsAPI::class.java))
    }

    fun loadLyrics(artist: String?, title: String?) {
        val disposable = service.getLyrics(artist, title)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                lyrics.value = it.lyrics
                result.value = true
            }, {
                result.value = false
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}