package com.team.attendancekt.ui.member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.team.attendancekt.databinding.MemberEditBinding
import kotlinx.android.synthetic.main.fragment_member_edit.*

class MemberEditFragment : Fragment() {

    lateinit var memberEditViewModel: MemberEditViewModel

    companion object {
        val KEY_MEMBER_ID = "member_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memberEditViewModel = ViewModelProviders.of(this).get(MemberEditViewModel::class.java)

        val id = arguments?.getInt(KEY_MEMBER_ID) ?: 0
        memberEditViewModel.init(id)
        memberEditViewModel.editMember?.observe(this, Observer {
            memberEditViewModel.member.value = it
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = MemberEditBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = memberEditViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSave.setOnClickListener {
            memberEditViewModel.save()
            findNavController().navigateUp()
        }
    }

}