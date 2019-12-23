package com.team.attendancekt.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Member(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var age: Int = 0,
    var phone: String = "",
    var email: String = "",
    var photo: String = "",
    var barcode: String = ""
)