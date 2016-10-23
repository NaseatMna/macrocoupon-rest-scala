package service_test.services

import api.Page
import com.google.inject.ImplementedBy
import models.Service

import scala.concurrent.Future

/**
 * Created by Acer on 10/20/2016.
 */
@ImplementedBy(classOf[ServService])
trait IServService {
  def insertService(service: Service): Future[Int]
  def updateService(id: Long, service: Service): Future[Int]
  def deleteService(id: Long): Future[Int]
  //def listAllService(sortingFields: Seq[(String, Boolean)], p: Int, s: Int): Future[Page[Service]]
  def filterServiceById(id: Long): Future[Service]
  def filterServiceByName(name: String): Future[Seq[Service]]
  def countService(): Future[Int]
  def sortService(mode: String): Future[Seq[Service]]
}
