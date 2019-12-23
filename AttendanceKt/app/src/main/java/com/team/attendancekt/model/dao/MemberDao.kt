package com.team.attendancekt.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.team.attendancekt.model.entity.Member

@Dao
interface MemberDao : CudDao<Member> {

    @Query("SELECT * FROM Member WHERE id = :id LIMIT 1")
    fun getMember(id: Int): LiveData<Member>

    @Query("SELECT * FROM Member")
    fun getAll(): LiveData<List<Member>>

}