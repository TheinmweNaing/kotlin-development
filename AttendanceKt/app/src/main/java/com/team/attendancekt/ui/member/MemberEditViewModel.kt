package com.team.attendancekt.ui.member

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.team.attendancekt.ServiceLocator
import com.team.attendancekt.model.entity.Member
import com.team.attendancekt.model.repo.MemberRepo

class MemberEditViewModel(application: Application) : AndroidViewModel(application) {

    val memberRepo: MemberRepo = ServiceLocator.getInstance(application).memberRepo()
    val member = MutableLiveData<Member>()
    var editMember: LiveData<Member>? = null

    fun save() {
        member.value?.also { memberRepo.save(it) }
    }

    fun init(id: Int) {
        if (id > 0) {
            editMember = memberRepo.getMember(id)
        } else {
            member.value = Member()
        }
    }
}