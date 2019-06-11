package org.simoexpo.kprice.example.routes.models

import org.simoexpo.kprice.example.models.User
import org.simoexpo.kprice.example.models.UserStatus.UserStatus
import org.simoexpo.kprice.example.routes.models

case class UserView(id: Long, userName: String, email: String, status: UserStatus)

object UserView {

  def from(user: User): UserView = models.UserView(user.id, user.userName, user.email, user.status)

}
