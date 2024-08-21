package com.example.whatsapp.Models

public class Users {

    var profilepic:String?=null
    var userName:String?=null
    var mail:String?=null
    var password:String?=null
    var userId:String?=null
    var lastMessage:String?=null

    constructor()

    constructor(
        profilepic: String?,
        userName: String?,
        mail: String?,
        userId: String?,
        password: String?,
        lastMessage: String?
    ) {
        this.profilepic = profilepic
        this.userName = userName
        this.mail = mail
        this.password=password
        this.userId = userId
        this.lastMessage = lastMessage
    }

    //SignUp Constructor
    constructor(
        userName: String?,
        mail: String?,
        password: String?
    ) {
        this.userName = userName
        this.mail = mail
        this.password=password
    }

    //SignIn Constructor
    constructor (
        mail: String?,
        password:String?
        ) {
        this.mail = mail
        this.password = password
    }



}