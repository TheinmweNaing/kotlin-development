package com.team.attendancekt.model.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface CudDao<E> {

    @Insert
    fun insert(entity: E)

    @Update
    fun update(entity: E)

    @Delete
    fun delete(entity: E)

}