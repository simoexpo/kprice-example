package org.simoexpo.kprice.example.models

object UserStatus extends Enumeration {

  type UserStatus = Value

  val Draft = Value(1, "Draft")
  val Valid = Value(2, "Valid")
  val Invalid = Value(3, "Invalid")
}
