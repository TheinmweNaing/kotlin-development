package com.team.attendancekt.model.repo

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.team.attendancekt.model.dao.AttendanceDao
import com.team.attendancekt.model.entity.Attendance
import com.team.attendancekt.model.entity.tuple.MemberAttendance

class AttendanceRepo(private val dao: AttendanceDao) {

    fun save(attendance: Attendance) {
        if (attendance.id > 0) {
            dao.update(attendance)
        } else {
            dao.insert(attendance)
        }
    }

    fun getAttendance(id: Long) = dao.getAttendance(id)

    fun delete(attendance: Attendance) = dao.delete(attendance)

    fun getAll(): LiveData<PagedList<MemberAttendance>> {
        return LivePagedListBuilder(dao.getAll(), 3).build()
    }
}