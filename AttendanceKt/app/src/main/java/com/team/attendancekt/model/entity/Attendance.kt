package com.team.attendancekt.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import org.joda.time.DateTime
import org.joda.time.LocalDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            parentColumns = ["id"],
            childColumns = ["member_id"],
            entity = Member::class
        )
    ]
)
data class Attendance(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "event_time")
    val eventTime: DateTime = DateTime.now(),
    @ColumnInfo(name = "member_id")
    val memberId: Int = 0
)