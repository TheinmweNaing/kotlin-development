package com.example.lyrics.model.service

import com.example.lyrics.model.api.LyricsAPI
import com.example.lyrics.model.dto.LyricsData
import io.reactivex.Observable

class LyricsService(private val api: LyricsAPI) {

    fun getLyrics(artist: String, title: String): Observable<LyricsData> {
        return api.getLyrics(artist, title)
    }
}