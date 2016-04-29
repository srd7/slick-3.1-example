package models

import slick.driver.H2Driver.api._

case class User(
  id: Int,
  name: String
)

class Users(tag: Tag) extends Table[User](tag, "USER") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME")
  def * = (id, name) <> (User.tupled, User.unapply)
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

case class Comment(
  id: Int,
  userId: Int,
  blogId: Int,
  body: String
)

class Comments(tag: Tag) extends Table[Comment](tag, "COMMENT") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def userId = column[Int]("USER_ID")
  def blogId = column[Int]("BLOG_ID")
  def body = column[String]("BODY")
  def * = (id, userId, blogId, body) <> (Comment.tupled, Comment.unapply)
}
