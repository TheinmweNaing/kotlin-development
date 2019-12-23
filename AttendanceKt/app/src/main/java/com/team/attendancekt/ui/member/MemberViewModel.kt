package com.team.attendancekt.ui.member

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.team.attendancekt.ServiceLocator
import com.team.attendancekt.model.entity.Member
import com.team.attendancekt.model.repo.MemberRepo

class MemberViewModel(application: Application) : AndroidViewModel(application) {

    val memberRepo: MemberRepo = ServiceLocator.getInstance(application).memberRepo()
    val members: LiveData<List<Member>> = getAllMembers()

    fun getAllMembers(): LiveData<List<Member>> = if (members == null) memberRepo.getAll() else members
}