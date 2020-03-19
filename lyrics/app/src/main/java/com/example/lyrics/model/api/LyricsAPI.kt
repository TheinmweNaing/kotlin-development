package com.example.lyrics.model.api

import com.example.lyrics.model.dto.LyricsData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface LyricsAPI {

    @GET("{artist}/{title}")
    fun getLyrics(
        @Path("artist") artist: String,
        @Path("title") title: String
    ): Observable<LyricsData>
}