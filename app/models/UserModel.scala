package models

import java.text.SimpleDateFormat
import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._
import slick.driver.PostgresDriver.api._
import com.github.tototoshi.slick.PostgresJodaSupport._

import scala.concurrent.{ Await, Future }

case class UserModel(user_id: Option[Long], user_email: String, user_first_name: String,
  user_last_name: String, user_password: String, user_confirm_password: String,
  user_token: String, user_active: Boolean, user_role: Long, create_time: DateTime)

object UserModel {

  val userReads: Reads[UserModel] = (
    (__ \ "user_id").readNullable[Long] and
    (__ \ "user_email").read[String] and
    (__ \ "user_first_name").read[String] and
    (__ \ "user_last_name").read[String] and
    (__ \ "user_password").read[String] and
    (__ \ "user_confirm_password").read[String] and
    (__ \ "user_token").read[String] and
    (__ \ "user_active").read[Boolean] and
    (__ \ "user_role").read[Long] and
    (__ \ "create_time").read[DateTime]
  )(UserModel.apply _)

  val userWrites: Writes[UserModel] = (
    (__ \ "user_id").writeNullable[Long] and
    (__ \ "user_email").write[String] and
    (__ \ "user_first_name").write[String] and
    (__ \ "user_last_name").write[String] and
    (__ \ "user_password").write[String] and
    (__ \ "user_confirm_password").write[String] and
    (__ \ "user_token").write[String] and
    (__ \ "user_active").write[Boolean] and
    (__ \ "user_role").write[Long] and
    (__ \ "create_time").write[DateTime]
  )(unlift(UserModel.unapply))

  implicit val userFormat: Format[UserModel] =
    Format(userReads, userWrites)
}

class UserTable(tag: Tag) extends Table[UserModel](tag, "user_tbl") {

  def id = column[Long]("user_id", O.PrimaryKey, O.AutoInc)
  def email = column[String]("user_email")
  def firstName = column[String]("user_first_name")
  def lastName = column[String]("user_last_name")
  def password = column[String]("user_password")
  def confirmPassword = column[String]("user_confirm_password")
  def token = column[String]("user_token")
  def active = column[Boolean]("user_active")
  def role = column[Long]("user_role")
  def createTime = column[DateTime]("create_time")
  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, email, firstName, lastName, password, confirmPassword, token, active, role, createTime) <> ((UserModel.apply _).tupled, UserModel.unapply _)
}

