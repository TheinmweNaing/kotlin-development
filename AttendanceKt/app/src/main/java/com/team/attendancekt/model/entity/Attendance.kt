package com.team.attendancekt.model.entity

import androidx.room.*
import org.joda.time.DateTime
import org.joda.time.LocalDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            parentColumns = ["id"],
            childColumns = ["member_id"],
            entity = Member::class
        )
    ], indices = [Index("member_id")]
)
data class Attendance(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "event_time")
    val eventTime: DateTime = DateTime.now(),
    var status: Status = Status.PRESENT,
    @ColumnInfo(name = "member_id")
    var memberId: Int = 0
)

enum class Status {
    PRESENT, ABSENT
}