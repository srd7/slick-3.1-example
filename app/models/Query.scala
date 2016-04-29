package models

import scala.concurrent.ExecutionContext

import slick.driver.H2Driver.api._

object Query {

  ////////////////////////////////////////////////////////////////
  // Level 0 (Single table query)

  /** Create user and return inserted user id */
  def createUser(user: User): DBIO[Int] = {
    users returning users.map(_.id) += user
  }

  /** Find all users */
  def getAllUser: DBIO[Seq[User]] = {
    users.result
  }

  /** Find user by id and update user's name */
  def updateUserName(user: User): DBIO[Int] = {
    users.filter(_.id === user.id).map(_.name).update(user.name)
  }

  /** Delete user by id */
  def deleteUserById(userId: Int): DBIO[Int] = {
    users.filter(_.id === userId).delete
  }

}
