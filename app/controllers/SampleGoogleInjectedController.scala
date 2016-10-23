package controllers

import com.google.inject.Inject
import play.api._
import play.api.mvc._
import com.google.inject.Singleton
import scala.concurrent.Future
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

@Singleton
class SampleGoogleInjectedController @Inject() (dbConfigProvider: DatabaseConfigProvider) extends Controller {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  def index = Action {
    Ok("Your new application is ready." + dbConfig)
  }

}