package org.simoexpo.kprice.example.models

import UserStatus.UserStatus

case class User(id: Long, userName: String, email: String, status: UserStatus)
