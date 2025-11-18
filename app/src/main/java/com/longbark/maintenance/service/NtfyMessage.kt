package com.longbark.maintenance.service

import com.google.gson.annotations.SerializedName

data class NtfyMessage(
    @SerializedName("id")
    val id: String,
    @SerializedName("time")
    val time: Long,
    @SerializedName("event")
    val event: String,
    @SerializedName("topic")
    val topic: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("title")
    val title: String?,
    @SerializedName("priority")
    val priority: Int?,
    @SerializedName("tags")
    val tags: List<String>?,
    @SerializedName("click")
    val click: String?,
    @SerializedName("actions")
    val actions: List<NtfyAction>?
)

data class NtfyAction(
    @SerializedName("action")
    val action: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("url")
    val url: String?,
    @SerializedName("method")
    val method: String?,
    @SerializedName("headers")
    val headers: Map<String, String>?,
    @SerializedName("body")
    val body: String?
)
