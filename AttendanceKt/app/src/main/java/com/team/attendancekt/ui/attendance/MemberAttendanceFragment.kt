package com.team.attendancekt.ui.attendance

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.team.attendancekt.R
import com.team.attendancekt.ui.attendance.edit.MemberAttendanceEditFragment
import kotlinx.android.synthetic.main.fragment_member_attendance.*

class MemberAttendanceFragment : Fragment() {

    private lateinit var viewModel: MemberAttendanceViewModel
    private lateinit var adapter: MemberAttendanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        adapter = MemberAttendanceAdapter()
        viewModel = ViewModelProviders.of(this)[MemberAttendanceViewModel::class.java]
        viewModel.attendances.observe(this, Observer {
            adapter.submitList(it)
        })
        viewModel.attendances
        adapter.adapterItemClickListener = object : MemberAttendanceAdapter.AdapterItemClickListener {
            override fun onClick(position: Int) {
                val args = Bundle()
                adapter.getItemAt(position)?.id?.let {
                    args.putLong(MemberAttendanceEditFragment.KEY_MEMBER_ATTENDANCE_ID, it)
                }
                findNavController().navigate(R.id.action_memberAttendanceFragment_to_memberAttendanceEditFragment, args)
            }

        }

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
        (activity as AppCompatActivity).supportActionBar?.title = "Attendance"

        recyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(view.context, DividerItemDecoration.VERTICAL))
            adapter = this@MemberAttendanceFragment.adapter
        }

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_memberAttendanceFragment_to_memberAttendanceEditFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_nav, menu)
        menu.findItem(R.id.memberAttendanceFragment).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.memberListFragment -> findNavController().navigate(R.id.memberListFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}