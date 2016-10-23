package models.repositories.impl
import com.google.inject.Inject
import models.repositories.IServicesRepo
import models.{ Service, TableService }
import play.api.db.slick.{ HasDatabaseConfig, DatabaseConfigProvider }
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import scala.concurrent.{ Await, Future }

/**
 * Created by Naseat on 10/14/2016.
 */

class ServicesRepo @Inject() (dbConfigProvider: DatabaseConfigProvider) extends IServicesRepo with HasDatabaseConfig[JdbcProfile] {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val services = TableQuery[TableService]

  /*
   Select row when id == id

   */
  private def FilterQuery(id: Long): Query[TableService, Service, Seq] = {

    services.filter(_.id === id)
  }

  /*Insert Service to Table Service*/
  override def insertService(service: Service): Future[Int] = {
    try dbConfig.db.run(services += service)
  }

  /*
    get all service from table service
  */
  override def listAllService(): Future[Seq[Service]] = {
    val query =
      for {
        services <- services if services != null
      } yield services
    try dbConfig.db.run(query.result)

  }

  /*
    Delete service if id equal given
 */
  override def deleteService(id: Long): Future[Int] = {
    try dbConfig.db.run(FilterQuery(id).delete)
  }

  /*
   Count amount service from row
   */
  override def countService(): Future[Int] = {

    try dbConfig.db.run(services.length.result)
    /* close db */
  }

  override def filterServiceById(id: Long): Future[Service] = {
    try dbConfig.db.run(FilterQuery(id).result.head)
    /*
    close connection
     */
  }

  /*
      update service name
 */
  override def updateService(service: Service): Future[Int] = {
    try dbConfig.db.run(FilterQuery(service.servicecategory_id.get).update(service))
  }

  /*Get Service by service name*/
  override def filterServiceByName(name: String): Future[Seq[Service]] = {
    try dbConfig.db.run(services.filter(_.name.toLowerCase like ("%" ++ name ++ "%").toLowerCase).result)
  }

  override def sortService(mode: String): Future[Seq[Service]] = {
    if (mode == "asc") { // sort with descending
      val query = services.sortBy(_.name.asc.nullsFirst)
      try dbConfig.db.run(query.result);
    } else { // sort with ascending
      val query = services.sortBy(_.name.desc.nullsFirst)
      try dbConfig.db.run(query.result);
    }
  }

}
