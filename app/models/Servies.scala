package models

import scala.concurrent.{ Await, Future }
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import slick.driver.PostgresDriver.api._
/**
 * Created by Naseat on 10/14/2016.
 */
case class Service(servicecategory_id: Option[Long], servicecategory_name: String, servicecategory_description: String)

object Service {

  implicit val serviceReads: Reads[Service] = (
    (JsPath \ "servicecategory_id").readNullable[Long] and
    (JsPath \ "servicecategory_name").read[String] and
    (JsPath \ "servicecategory_description").read[String]
  )(Service.apply _)
  implicit val serviceWrites: Writes[Service] = (
    (JsPath \ "servicecategory_id").writeNullable[Long] and
    (JsPath \ "servicecategory_name").write[String] and
    (JsPath \ "servicecategory_description").write[String]
  )(unlift(Service.unapply _))

  implicit val serviceFormat: Format[Service] = Format(serviceReads, serviceWrites)
}
class TableService(tag: Tag) extends Table[Service](tag, "tblServiceCategory") {
  def id = column[Long]("servicecategory_id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("servicecategory_name")
  def description = column[String]("servicecategory_description")
  def * = (id.?, name, description) <> ((Service.apply _).tupled, Service.unapply _)

}
