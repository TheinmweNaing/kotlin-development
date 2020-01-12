package com.team.attendancekt.ui.chat

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.team.attendancekt.AttendanceApplication
import com.team.attendancekt.R
import com.team.attendancekt.model.entity.ChatMessage
import com.team.attendancekt.model.entity.MessageType
import io.reactivex.disposables.CompositeDisposable
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent

class ChatMessageViewModel(application: Application) : AndroidViewModel(application) {

    private var stompClient: StompClient
    private var objectMapper: ObjectMapper
    private val disposable = CompositeDisposable()

    private val list = mutableListOf<ChatMessage>()

    val messages = MutableLiveData<List<ChatMessage>>()
    val error = MutableLiveData<String>()
    val connectResult = MutableLiveData<Boolean>()

    init {
        val baseUrl = application.getString(R.string.server_url)
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, baseUrl)
        objectMapper = jacksonObjectMapper()
    }

    fun connect(user: String) {
        AttendanceApplication.currentUser = user
        stompClient.connect()
        val d = stompClient.lifecycle().subscribe { event ->
            when (event.type) {
                LifecycleEvent.Type.OPENED -> {
                    stompClient.topic("/msg/public").subscribe {
                        val message = objectMapper.readValue(it.payload, ChatMessage::class.java)
                        list.add(message)

                        val data = mutableListOf<ChatMessage>()
                        data.addAll(list)
                        messages.postValue(data)
                    }
                    val joinMsg = ChatMessage(MessageType.JOIN, sender = user)
                    stompClient.send("/app/chat.addUser", objectMapper.writeValueAsString(joinMsg))
                        .subscribe()

                    connectResult.postValue(true)
                }
                LifecycleEvent.Type.ERROR -> {
                    error.postValue(event.exception.message)
                    connectResult.postValue(false)
                }
                else -> {
                }
            }

        }
        disposable.add(d)
    }

    fun send(text: String) {
        val msg = ChatMessage(MessageType.CHAT, text, AttendanceApplication.currentUser)
        stompClient.send("/app/chat.sendMessage", objectMapper.writeValueAsString(msg)).subscribe()
    }

    fun disconnect() {
        stompClient.disconnect()
        disposable.dispose()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}