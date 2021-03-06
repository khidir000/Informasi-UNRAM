package com.example.zhack.share_ie.model

import com.google.gson.annotations.SerializedName

data class User (

    @SerializedName("status")
    var status:Int=0,
    @SerializedName("message")
    var message:String?=null,
    @SerializedName("id")
    var id:String?=null,
    @SerializedName("token")
    var token:String? =null,
    @SerializedName("api_key")
    var api_key:String? =null
    )