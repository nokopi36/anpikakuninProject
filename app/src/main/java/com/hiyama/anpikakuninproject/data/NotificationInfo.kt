package com.hiyama.anpikakuninproject.data

object NotificationInfo {
    var title = ""
    var content = ""

    fun initialize(notification: Notification) {
        title = notification.title
        content = notification.content
    }
}