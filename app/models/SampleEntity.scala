package models;

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class SampleEntity(
  id: Option[Int],
  message: String
)

object SampleEntity {
  implicit val messageReads: Reads[SampleEntity] = (
    (JsPath \ "id").readNullable[Int] and
    (JsPath \ "message").read[String]
  )(SampleEntity.apply _)

  implicit val messageWrites: Writes[SampleEntity] = (
    (JsPath \ "id").writeNullable[Int] and
    (JsPath \ "message").write[String]
  )(unlift(SampleEntity.unapply _))
}