package com.example.wabprojectredo.classes

class ChatMessage(val id:String,
                  val text:String,
                  val fromId:String,
                  val fromUsername:String?,
                  val roomName:String,
                  val timestamp:String){

    constructor() : this("","","","","","")
}