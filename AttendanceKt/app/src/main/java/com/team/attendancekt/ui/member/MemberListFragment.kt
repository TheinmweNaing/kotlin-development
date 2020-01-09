package com.team.attendancekt.ui.member

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.team.attendancekt.R
import kotlinx.android.synthetic.main.fragment_member_list.*

class MemberListFragment : Fragment() {

    lateinit var memberAdapter: MemberAdapter
    lateinit var viewModel: MemberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        memberAdapter = MemberAdapter()
        viewModel = ViewModelProviders.of(this).get(MemberViewModel::class.java)
        viewModel.members.observe(this, Observer {
            memberAdapter.submitList(it)
        })
        viewModel.getAllMembers()
        memberAdapter.adapterItemClickListener = object : MemberAdapter.AdapterItemClickListener {
            override fun onClick(position: Int) {
                val args = Bundle()
                args.putInt(MemberEditFragment.KEY_MEMBER_ID, memberAdapter.getItemAt(position).id)
                findNavController().navigate(
                    R.id.action_memberListFragment_to_memberEditFragment,
                    args
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_member_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Member"

        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                view.context,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.adapter = memberAdapter

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_memberListFragment_to_memberEditFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_nav, menu)
        menu.findItem(R.id.memberListFragment).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.memberAttendanceFragment -> findNavController().navigate(R.id.memberAttendanceFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}