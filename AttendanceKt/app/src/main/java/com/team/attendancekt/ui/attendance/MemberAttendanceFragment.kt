package com.team.attendancekt.ui.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.team.attendancekt.R
import kotlinx.android.synthetic.main.fragment_member_attendance.*

class MemberAttendanceFragment : Fragment() {

    private lateinit var viewModel: MemberAttendanceViewModel
    private lateinit var adapter: MemberAttendanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = MemberAttendanceAdapter()
        viewModel = ViewModelProviders.of(this)[MemberAttendanceViewModel::class.java]
        viewModel.attendances.observe(this, Observer {
            adapter.submitList(it)
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_member_attendance, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
            adapter = this@MemberAttendanceFragment.adapter
        }

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_memberAttendanceFragment_to_memberAttendanceEditFragment)
        }
    }
}