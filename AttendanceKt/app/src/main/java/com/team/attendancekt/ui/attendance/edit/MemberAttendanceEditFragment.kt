package com.team.attendancekt.ui.attendance.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.team.attendancekt.R
import com.team.attendancekt.databinding.FragmentAttendanceEditBinding
import com.team.attendancekt.model.entity.Member
import com.team.attendancekt.model.entity.Status
import kotlinx.android.synthetic.main.fragment_attendance_edit.*

class MemberAttendanceEditFragment : Fragment() {

    lateinit var memberAttendanceEditViewModel: MemberAttendanceEditViewModel
    lateinit var memberArrayAdapter: ArrayAdapter<Member>
    lateinit var binding: FragmentAttendanceEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        memberAttendanceEditViewModel =
            ViewModelProviders.of(this)[MemberAttendanceEditViewModel::class.java]
        memberArrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        memberAttendanceEditViewModel.members.observe(this, Observer {
            memberArrayAdapter.addAll(it)
        })

        memberAttendanceEditViewModel.attendanceId.value = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAttendanceEditBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.vm = memberAttendanceEditViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerMember.adapter = memberArrayAdapter
        edMember.setOnClickListener {
            spinnerMember.performClick()
        }
        spinnerMember.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                memberAttendanceEditViewModel.member.value = memberArrayAdapter.getItem(position)
            }

        }

        radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when(checkedId) {
                    R.id.rbPresent -> memberAttendanceEditViewModel.attendance.value?.status = Status.PRESENT
                    R.id.rbAbsent -> memberAttendanceEditViewModel.attendance.value?.status = Status.ABSENT
                }
            }

        })

        btnSave.setOnClickListener {
            memberAttendanceEditViewModel.save()
            findNavController().navigateUp()
        }

    }
}