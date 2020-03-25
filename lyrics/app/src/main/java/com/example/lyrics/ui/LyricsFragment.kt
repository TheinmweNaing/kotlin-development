package com.example.lyrics.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.lyrics.MainActivity
import com.example.lyrics.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.layout_lyrics.*

class LyricsFragment : Fragment() {

    lateinit var viewModel: LyricsViewModel

    companion object {
        const val ARTIST = "artist"
        const val TITLE = "title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val activity = requireActivity() as MainActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lyrics, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val activity = requireActivity() as MainActivity
        activity.showProgress()
        viewModel = ViewModelProvider(this)[LyricsViewModel::class.java]

        val artist = arguments?.getString(ARTIST)
        val title = arguments?.getString(TITLE)

        viewModel.loadLyrics(artist, title)
        viewModel.lyrics.observe(viewLifecycleOwner, Observer {
            activity.hideProgress()
            txtLyrics.text = it
            txtArtist.text = artist
            txtTitle.text = title
        })

        viewModel.result.observe(viewLifecycleOwner, Observer {
            activity.hideProgress()
            if (!it) view?.apply {
                Snackbar.make(this, "Not found.", Snackbar.LENGTH_SHORT).show()
            }
        })
        txtLyrics.movementMethod = ScrollingMovementMethod()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}