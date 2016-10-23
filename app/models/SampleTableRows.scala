package models;

import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import play.api.db.slick.DatabaseConfigProvider
import play.api._

class SampleTableRows(tag: Tag) extends Table[SampleEntity](tag, "messages") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def message = column[String]("message")

  def * = (id.?, message) <> ((SampleEntity.apply _).tupled, SampleEntity.unapply _)
}