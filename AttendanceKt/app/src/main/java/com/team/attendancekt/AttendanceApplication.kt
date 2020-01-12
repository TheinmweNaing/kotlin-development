package com.team.attendancekt

import android.app.Application

class AttendanceApplication : Application() {

    companion object {
        lateinit var currentUser: String
    }
}