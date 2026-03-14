package com.dev.nagda.features.requests.model

import com.dev.nagda.R

enum class RequestStatus(val label: String, val colorRes: Int) {
    SENT        ("تم الإرسال",    R.color.status_sent),
    RECEIVED    ("تم الاستلام",   R.color.status_received),
    IN_PROGRESS ("جاري التعامل", R.color.status_in_progress),
    ON_THE_WAY  ("في الطريق",    R.color.status_on_the_way),
    DONE        ("تم التعامل",   R.color.status_done),
    CANCELLED   ("تم الالغاء",   R.color.status_cancelled)
}