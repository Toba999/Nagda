package com.dev.nagda.data.model

data class RequestModel(
    val id: String = "",
    val uid: String = "",
    val type: RequestType = RequestType.ACCIDENT,
    val status: RequestStatus = RequestStatus.SENT,
    val location: String = "",
    val details: String = "",
    val createdAt: Long = System.currentTimeMillis()
)