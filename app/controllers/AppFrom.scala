package controllers

import play.api.data.Form
import play.api.data.Forms._

import models._

object AppForm {
  def userForm: Form[User] = Form (
    mapping(
      "id"   -> default(number(min = 1), -1),
      "name" -> nonEmptyText
    )(User.apply _)(User.unapply)
  )
}
