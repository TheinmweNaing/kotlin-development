package com.team.attendancekt.model.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.team.attendancekt.model.entity.Attendance
import com.team.attendancekt.model.entity.tuple.MemberAttendance

@Dao
interface AttendanceDao : CudDao<Attendance> {

    @Query("SELECT * FROM ATTENDANCE WHERE id = :id LIMIT 1")
    fun getAttendance(id: Long): LiveData<Attendance>

    @Query("SELECT a.id,m.name,a.event_time,a.status FROM ATTENDANCE a " +
            "LEFT JOIN MEMBER m ON a.member_id = m.id " +
            "ORDER BY a.event_time DESC")
    fun getAll(): DataSource.Factory<Int, MemberAttendance>
}