package com.team.attendancekt.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team.attendancekt.model.dao.MemberDao
import com.team.attendancekt.model.entity.Member

@Database(entities = [
    Member::class
], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao

}