package com.example.lyrics

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.lyrics.ui.LyricsViewModel
import kotlinx.android.synthetic.main.activity_lyrics.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics)
        val viewModel: LyricsViewModel by viewModels()

        btnSearch.setOnClickListener {
            val artist = edArtitst.text.toString()
            val title = edTitle.text.toString()

            if (artist.isEmpty()) edArtitst.setError("Please Enter Artist!")
            if (title.isEmpty()) edTitle.setError("Please Enter Song Title!")

            viewModel.loadLyrics(artist, title)
            viewModel.lyrics.observe(this, Observer {
                txtLyrics.text = it
            })
            txtLyrics.movementMethod = ScrollingMovementMethod()
        }
    }
}
