package controllers

import play.api.mvc._
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import slick.driver.H2Driver

import javax.inject.Inject

class AppController @Inject() (dbConfigProvider: DatabaseConfigProvider) extends Controller {
  val db = dbConfigProvider.get[H2Driver].db

  def index = Action.async {
    import slick.driver.H2Driver.api._

    db.run(
      sql"SELECT * FROM USER".as[(Int, String)]
    ).map(userList =>
      Ok(userList.map{ case (id, name) =>
        s"${id} => ${name}"
      }.mkString("\n"))
    )
  }
}
