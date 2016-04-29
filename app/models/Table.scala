package models

import slick.driver.H2Driver.api._

case class User(
  id: Int,
  name: String,
  count: Int
)

class Users(tag: Tag) extends Table[User](tag, "USER") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  def count = column[Int]("COUNT")
  def * = (id, name, count) <> (User.tupled, User.unapply)
}

case class Blog(
  id: Int,
  userId: Int,
  title: String,
  body: String
)

class Blogs(tag: Tag) extends Table[Blog](tag, "BLOG") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def userId = column[Int]("USER_ID")
  def title = column[String]("TITLE")
  def body = column[String]("BODY")
  def * = (id, userId, title, body) <> (Blog.tupled, Blog.unapply)
}
