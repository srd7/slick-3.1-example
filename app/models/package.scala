import slick.driver.H2Driver.api._

package object models {
  val users = TableQuery[Users]
  val blogs = TableQuery[Blogs]
}
