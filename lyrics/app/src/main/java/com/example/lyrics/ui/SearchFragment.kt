package com.example.lyrics.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.lyrics.R
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSearch.setOnClickListener {
            val artist = edArtist.text.toString()
            val title = edTitle.text.toString()
            var count = 0
            if (artist.isEmpty()) {
                layoutArtist.error = getString(R.string.error_artist)
                count++
            }

            if (title.isEmpty()) {
                layoutTitle.error = getString(R.string.error_title)
                count++
            }

            if (count == 0) {
                val args = Bundle()
                args.putString(LyricsFragment.ARTIST, artist)
                args.putString(LyricsFragment.TITLE, title)
                findNavController().navigate(R.id.action_searchFragment_to_lyricsFragment, args)
            }

            edArtist.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty()!!) {
                        layoutArtist.error = null
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            })
            edTitle.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.isNotEmpty()!!) {
                        layoutTitle.error = null
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

            })
        }
    }

}