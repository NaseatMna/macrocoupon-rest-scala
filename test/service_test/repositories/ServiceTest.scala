package service_test.repositories

import com.google.inject.Inject
import models.repositories.IServicesRepo
import models.{ Service, TableService }
import play.api.db.slick.{ HasDatabaseConfig, DatabaseConfigProvider }
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import scala.concurrent.{ Await, Future }

/**
 * Created by Naseat on 10/20/2016.
 */
class ServiceTest @Inject() (dbConfigProvider: DatabaseConfigProvider) extends IServiceTest with HasDatabaseConfig[JdbcProfile] {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val services = TableQuery[TableService]
  /*
  Select row when id == id

  */
  private def FilterQuery(id: Long): Query[TableService, Service, Seq] = {

    services.filter(_.id === id)
  }
  override def insertService(service: Service): Future[Int] = {
    try dbConfig.db.run(services += service)
  }

  override def deleteService(id: Long): Future[Int] = {
    try dbConfig.db.run(FilterQuery(id).delete)
  }

  override def countService(): Future[Int] = {
    try dbConfig.db.run(services.length.result)
  }

  override def sortService(mode: String): Future[Seq[Service]] = ???

  override def filterServiceById(id: Long): Future[Service] = {
    try dbConfig.db.run(FilterQuery(id).result.head)
  }

  //override def listAllService(sortingFields: Seq[(String, Boolean)], p: Int, s: Int): Future[Page[Service]] = ???

  override def filterServiceByName(name: String): Future[Seq[Service]] = {
    try dbConfig.db.run(services.filter(_.name.toLowerCase like ("%" ++ name ++ "%").toLowerCase).result)
  }

  override def updateService(id: Long, service: Service): Future[Int] = {
    try dbConfig.db.run(FilterQuery(service.servicecategory_id.get).update(service))
  }

}
