package com.team.attendancekt.ui.attendance.edit

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.team.attendancekt.MainActivity
import com.team.attendancekt.R
import com.team.attendancekt.databinding.FragmentAttendanceEditBinding
import com.team.attendancekt.model.entity.Member
import com.team.attendancekt.model.entity.Status
import kotlinx.android.synthetic.main.fragment_attendance_edit.*

class MemberAttendanceEditFragment : Fragment() {

    lateinit var memberAttendanceEditViewModel: MemberAttendanceEditViewModel
    lateinit var memberArrayAdapter: ArrayAdapter<Member>
    lateinit var binding: FragmentAttendanceEditBinding

    companion object {
        val KEY_MEMBER_ATTENDANCE_ID = "member_attendance_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity:MainActivity = requireActivity() as MainActivity
        activity.switchToggle(false)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        memberAttendanceEditViewModel =
            ViewModelProviders.of(this)[MemberAttendanceEditViewModel::class.java]
        memberArrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1)
        memberAttendanceEditViewModel.members.observe(this, Observer {
            memberArrayAdapter.addAll(it)
        })

        memberAttendanceEditViewModel.attendance.observe(this, Observer {
            btnDelete.visibility = if (it.id > 0) View.VISIBLE else View.GONE
            when(it.status) {
                Status.PRESENT -> rbPresent.isChecked = true
                Status.ABSENT -> rbAbsent.isChecked = true
            }

            memberAttendanceEditViewModel.memberId.value = it.memberId
        })
        memberAttendanceEditViewModel.attendanceId.value = arguments?.getLong(KEY_MEMBER_ATTENDANCE_ID) ?: 0
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

        edMember.setOnClickListener {
           AlertDialog.Builder(requireContext())
               .setTitle("Choose Member")
               .setAdapter(memberArrayAdapter) { di, i ->
                   memberAttendanceEditViewModel.memberId.value = memberArrayAdapter.getItem(i)?.id
                   di.dismiss()
               }
               .show()

            val activity:MainActivity = requireActivity() as MainActivity
            activity.hideKeyBoard()
        }

        radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId) {
                    R.id.rbPresent -> memberAttendanceEditViewModel.attendance.value?.status =
                        Status.PRESENT
                    R.id.rbAbsent -> memberAttendanceEditViewModel.attendance.value?.status =
                        Status.ABSENT
                }
            }

        })

        btnSave.setOnClickListener {
            memberAttendanceEditViewModel.save()
            findNavController().navigateUp()
        }

        btnDelete.setOnClickListener {
            memberAttendanceEditViewModel.delete()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val activity:MainActivity = requireActivity() as MainActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.switchToggle(true)
        activity.hideKeyBoard()
    }
}