package models

import play.api.libs.json._
import scala.concurrent.{ Await, Future }
// JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax
import slick.driver.PostgresDriver.api._

/**
 * Created by User on 10/5/2016.
 */
/**
 * case class Store a new type
 * use for create type of store table
 * @param store_id
 * @param store_name
 *
 *
 */
case class Store(store_id: Option[Long], store_name: String)

object Store {
  implicit val storeReads: Reads[Store] = (
    (JsPath \ "store_id").readNullable[Long] and
    (JsPath \ "store_name").read[String]
  )(Store.apply _)

  implicit val storeWrites: Writes[Store] = (
    (JsPath \ "store_id").writeNullable[Long] and
    (JsPath \ "store_name").write[String]
  )(unlift(Store.unapply _))

  implicit val storeFormat: Format[Store] = Format(storeReads, storeWrites)
}

class TableStore(tag: Tag) extends Table[Store](tag, "tblstore") {
  def id = column[Long]("store_id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("store_name")

  def * = (id.?, name) <> ((Store.apply _).tupled, Store.unapply _)
}
