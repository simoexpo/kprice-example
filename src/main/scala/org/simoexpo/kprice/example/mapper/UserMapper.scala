package org.simoexpo.kprice.example.mapper

import org.simoexpo.kprice.example.models.{User, UserStatus}
import org.simoexpo.kprice.example.routes.models.UserSignUpContract

import scala.util.Random

object UserMapper {

  def from(contract: UserSignUpContract): User = User(Random.nextLong.abs, contract.userName, contract.email, UserStatus.Draft)

}
