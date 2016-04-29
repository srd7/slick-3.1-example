package models

import scala.concurrent.ExecutionContext

import slick.driver.H2Driver.api._

object Query {

  ////////////////////////////////////////////////////////////////
  // Single table query

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

  ////////////////////////////////////////////////////////////////
  // Multiple table query

  /** Find blog with user */
  def getBlogWithUser(blogId: Int): DBIO[Option[(Blog, User)]] = {
    val query = for {
      blog <- blogs if blog.id === blogId
      user <- users if user.id === blog.userId
    } yield (blog, user)

    query.result.headOption
  }

  ////////////////////////////////////////////////////////////////
  // Multiple query

  /** Find user and his/her blogs */
  def getUserWithBlog(userId: Int)(implicit ec: ExecutionContext): DBIO[Option[(User, Seq[Blog])]] = {
    for {
      userOp <- users.filter(_.id === userId).result.headOption
      // If user does not exist, next query (finding blog) is not necessary
      blogs  <- userOp.fold[DBIO[Seq[Blog]]](
        // Case userOp is None
        DBIO.successful(Nil)
      )(user =>
        // Case userOp is Some(user)
        blogs.filter(_.userId === user.id).result
      )
    } yield userOp.map(_ -> blogs)
  }

  /**
   * Create blog
   *
   * Increment User#count at the same time.
   */
  def createBlog(blog: Blog)(implicit ec: ExecutionContext): DBIO[Unit] = {
    val action = for {
      userOp <- users.filter(_.id === blog.userId).result.headOption
      // If user does not exist, next query is not necessary
      _      <- userOp.fold[DBIO[Int]](
        DBIO.successful(1)
      )(user =>
        blogs += blog
      )
      _      <- userOp.fold[DBIO[Int]](
        DBIO.successful(1)
      )(user =>
        users.filter(_.id === user.id).map(_.count).update(user.count + 1)
      )
    } yield ()

    action.transactionally
  }

}
