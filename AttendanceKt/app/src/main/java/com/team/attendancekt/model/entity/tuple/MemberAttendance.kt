package com.team.attendancekt.model.entity.tuple

import androidx.room.ColumnInfo
import com.team.attendancekt.model.entity.Status
import org.joda.time.DateTime

data class MemberAttendance(
    var id: Long = 0,
    var name: String = "",
    @ColumnInfo(name = "event_time")
    var eventTime: DateTime = DateTime.now(),
    var status: Status = Status.PRESENT
)