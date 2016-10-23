package models.manage_products

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import slick.driver.PostgresDriver.api._
/**
 * Created by Oudam on 10/12/2016.
 */
case class User(user_id: Option[Int], username: String, gender: String)
object User {
  val userReads: Reads[User] = (
    (__ \ "user_id").readNullable[Int] and
    (__ \ "username").read[String] and
    (__ \ "gender").read[String]
  )(User.apply _)

  val userWrites: Writes[User] = (
    (__ \ "user_id").writeNullable[Int] and
    (__ \ "username").write[String] and
    (__ \ "gender").write[String]
  )(unlift(User.unapply))

  implicit val userFormat: Format[User] =
    Format(userReads, userWrites)
}

class TUser(tag: Tag) extends Table[User](tag, "user_tbl") {

  def id = column[Int]("user_id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username")
  def gender = column[String]("gender")
  def * = (id.?, username, gender) <> ((User.apply _).tupled, User.unapply)

}
/*
object TUser{
  lazy val query = TableQuery[TUser]
}*/
