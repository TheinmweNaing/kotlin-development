package com.team.attendancekt.ui.attendance

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.team.attendancekt.ServiceLocator
import com.team.attendancekt.model.entity.tuple.MemberAttendance

class MemberAttendanceViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ServiceLocator.getInstance(application).attendanceRepo

    val attendances: LiveData<PagedList<MemberAttendance>> by lazy { repo.getAll() }
}