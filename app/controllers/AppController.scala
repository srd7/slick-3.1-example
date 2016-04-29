package controllers

import scala.concurrent.Future

import play.api.mvc._
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import slick.SlickException
import slick.driver.H2Driver

import javax.inject.Inject

import models.Query

class AppController @Inject() (dbConfigProvider: DatabaseConfigProvider) extends Controller {
  val db = dbConfigProvider.get[H2Driver].db

  private[this] val recovery: PartialFunction[Throwable, Result] = {
    case e: SlickException => InternalServerError(views.html.error("db.access.error"))
    case e: Throwable => InternalServerError(views.html.error("unknown.error"))
  }

  private[this] val redirectToUser = Redirect(routes.AppController.users)

  def index = Action {
    Ok(views.html.index())
  }

  def users = Action.async {
    db.run(
      Query.getAllUser
    ).map(users =>
      Ok(views.html.users.index(users))
    ).recover(recovery)
  }

  def createUser = Action.async { implicit request =>
    AppForm.userForm.bindFromRequest.fold(
      { formWithErrors =>
        Future.successful(redirectToUser)
      },
      { user =>
        db.run(
          Query.createUser(user)
        ).map(_ =>
          redirectToUser
        ).recover(recovery)
      }
    )
  }

  def updateUser(userId: Int) = Action.async { implicit request =>
    AppForm.userForm.bindFromRequest.fold(
      { formWithErrors =>
        Future.successful(redirectToUser)
      },
      { user =>
        db.run(
          Query.updateUserName(user.copy(id = userId))
        ).map(_ =>
          redirectToUser
        ).recover(recovery)
      }
    )
  }

  def deleteUser(userId: Int) = Action.async {
    db.run(
      Query.deleteUserById(userId)
    ).map(_ =>
      redirectToUser
    ).recover(recovery)
  }

  def showUser(userId: Int) = Action.async {
    db.run(
      Query.getUserWithBlog(userId)
    ).map {
      // If user does not exist
      case None => NotFound(views.html.error("user.not.found"))
      case Some((user, blogs)) =>
        Ok(views.html.users.show(user, blogs))
    }.recover(recovery)
  }

  def createBlog(userId: Int) = Action.async { implicit request =>
    AppForm.blogForm.bindFromRequest.fold(
      { formWithErrors =>
        Future.successful(Redirect(routes.AppController.showUser(userId)))
      },
      { blog =>
        db.run(
          Query.createBlog(blog.copy(userId = userId))
        ).map(_ =>
          Redirect(routes.AppController.showUser(userId))
        ).recover(recovery)
      }
    )
  }

  def showBlog(userId: Int, blogId: Int) = Action.async { implicit request =>
    db.run(
      Query.getBlogWithUser(blogId)
    ).map {
      // If blog does not exist
      case None => NotFound(views.html.error("blog.not.found"))
      case Some((blog, user)) =>
        Ok(views.html.users.blog(blog, user))
    }.recover(recovery)
  }

}
