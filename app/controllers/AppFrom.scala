package controllers

import play.api.data.Form
import play.api.data.Forms._

import models._

object AppForm {
  def userForm: Form[User] = Form (
    mapping(
      "id"    -> default(number(min = 1), -1),
      "name"  -> nonEmptyText,
      "count" -> default(number(max = 0, min = 0), 0)
    )(User.apply _)(User.unapply)
  )

  def blogForm: Form[Blog] = Form (
    mapping (
      "id"     -> default(number(min = 1), -1),
      "userId" -> default(number(min = 1), -1),
      "title"  -> nonEmptyText,
      "body"   -> nonEmptyText
    )(Blog.apply _)(Blog.unapply)
  )
}
