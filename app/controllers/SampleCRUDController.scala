package controllers

import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfig }
import play.api.libs.json._
import play.api.mvc._
import models.SampleEntity
import models.SampleTableRows
import play.api.i18n.MessagesApi

class SampleCRUDController @Inject() (
  val messagesApi: MessagesApi,
  protected val dbConfigProvider: DatabaseConfigProvider
)
    extends api.ApiController with HasDatabaseConfig[JdbcProfile] {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  val tablerows = TableQuery[SampleTableRows]

  def findQuery(id: Int) = tablerows.filter(_.id === id)

  def index = Action.async {
    val result = dbConfig.db.run(tablerows.result)
    result.map(msgs => Ok(Json.obj("status" -> "Ok", "messages" -> Json.toJson(msgs))))
  }

  def create = Action.async(BodyParsers.parse.json) { request =>
    val sampleEntity = request.body.validate[SampleEntity]
    sampleEntity.fold(
      errors => Future(BadRequest(Json.obj(
        "status" -> "Parsing message failed",
        "error" -> JsError.toJson(errors)
      ))),
      sampleEntity =>
        dbConfig.db.run(tablerows returning tablerows += sampleEntity).map(m =>
          Ok(Json.obj("status" -> "Success", "message" -> Json.toJson(m))))
    )
  }
  def show(id: Int) = Action.async {
    val sampleEntity = dbConfig.db.run(findQuery(id).result.head)
    sampleEntity.map(msg => Ok(Json.obj("status" -> "Ok", "message" -> Json.toJson(msg))))
  }
  def update(id: Int) = Action.async(BodyParsers.parse.json) { request =>
    val sampleEntity = request.body.validate[SampleEntity]
    sampleEntity.fold(
      errors => Future(BadRequest(Json.obj(
        "status" -> "Message update failed",
        "error" -> JsError.toJson(errors)
      ))),
      sampleEntity => {
        dbConfig.db.run(findQuery(id).update(sampleEntity))
        Future(Ok(Json.obj("status" -> "Ok", "message" -> Json.toJson(sampleEntity))))
      }
    )
  }
  def delete(id: Int) = Action.async {
    dbConfig.db.run(findQuery(id).delete).map(m => Ok(Json.obj("status" -> "Ok")))
  }

}