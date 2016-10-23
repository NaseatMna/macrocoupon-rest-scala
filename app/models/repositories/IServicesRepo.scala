package models.repositories

import com.google.inject.ImplementedBy
import models.Service
import models.repositories.impl.ServicesRepo
import scala.concurrent.Future

/**
 * Created by Naseat on 10/14/2016.
 */
@ImplementedBy(classOf[ServicesRepo])
trait IServicesRepo {
  def insertService(service: Service): Future[Int]
  def updateService(service: Service): Future[Int]
  def deleteService(id: Long): Future[Int]
  def listAllService(): Future[Seq[Service]]
  def filterServiceById(id: Long): Future[Service]
  def filterServiceByName(name: String): Future[Seq[Service]]
  def countService(): Future[Int]
  def sortService(mode: String): Future[Seq[Service]]
}
