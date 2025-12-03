package com.vero.app.data.remote

import com.google.gson.annotations.SerializedName

data class TaskResponse(
    @SerializedName("task") val task: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("colorCode") val colorCode: String?
)
