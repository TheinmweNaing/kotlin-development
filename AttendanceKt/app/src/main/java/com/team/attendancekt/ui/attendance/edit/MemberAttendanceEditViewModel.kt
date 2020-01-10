package com.team.attendancekt.ui.attendance.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.team.attendancekt.ServiceLocator
import com.team.attendancekt.model.entity.Attendance
import com.team.attendancekt.model.entity.Member

class MemberAttendanceEditViewModel(application: Application) : AndroidViewModel(application) {

    private val attendanceRepo = ServiceLocator.getInstance(application).attendanceRepo
    private val memberRepo = ServiceLocator.getInstance(application).memberRepo()

    val attendanceId = MutableLiveData<Long>()
    val memberId = MutableLiveData<Int>()
    val member: LiveData<Member> = Transformations.switchMap(memberId) {
        memberRepo.getMember(it)
    }

    val attendance: LiveData<Attendance> = Transformations.switchMap(attendanceId) {
        if (it > 0) {
            attendanceRepo.getAttendance(it)
        } else {
            val liveData = MutableLiveData<Attendance>()
            liveData.value = Attendance()
            liveData
        }
    }
    val members: LiveData<List<Member>> by lazy { memberRepo.getAll() }

    fun save() {
        attendance.value?.also {
            it.memberId = member.value?.id!!
            attendanceRepo.save(it)
        }
    }

    fun delete() {
        attendance.value?.also { attendanceRepo.delete(it) }
    }

}