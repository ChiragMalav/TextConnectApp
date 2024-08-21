package com.example.whatsapp.Models

public class Message {
    var uId : String? = null
    var message : String? = null
    var timestamp : Long? = null

    constructor()

    public constructor(uId: String?, message: String?, timestamp: Long?) {
        this.uId = uId
        this.message = message
        this.timestamp = timestamp
    }

    public constructor(uId: String?, message: String?) {
        this.uId = uId
        this.message = message
    }

}