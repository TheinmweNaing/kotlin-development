package com.team.attendancekt.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.team.attendancekt.model.dao.AttendanceDao
import com.team.attendancekt.model.dao.MemberDao
import com.team.attendancekt.model.entity.Attendance
import com.team.attendancekt.model.entity.Member
import com.team.attendancekt.model.entity.Status
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

@Database(
    entities = [
        Member::class, Attendance::class
    ], version = 1, exportSchema = false
)
@TypeConverters(com.team.attendancekt.model.TypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao
    abstract fun attendanceDao(): AttendanceDao
}

class TypeConverters {

    companion object {

        @JvmStatic
        @TypeConverter
        fun getLocalDateTime(timeStamp: Long): DateTime {
            return DateTime(timeStamp, DateTimeZone.getDefault())
        }

        @JvmStatic
        @TypeConverter
        fun setLocalDateTime(dateTime: DateTime): Long {
            return dateTime.millis
        }

        @JvmStatic
        @TypeConverter
        fun getAttendanceStatus(value: Int): Status = Status.values()[value]

        @JvmStatic
        @TypeConverter
        fun setAttendanceStatus(status: Status): Int = status.ordinal
    }
}