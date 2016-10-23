package service_test.services

import api.Page
import com.google.inject.Inject
import models.Service
import service_test.repositories.IServiceTest

import scala.concurrent.Future

/**
 * Created by Acer on 10/20/2016.
 */
class ServService @Inject() (repo: IServiceTest) extends IServService {
  override def insertService(service: Service): Future[Int] = { repo.insertService(service) }

  override def deleteService(id: Long): Future[Int] = { repo.deleteService(id) }

  override def countService(): Future[Int] = { repo.countService() }

  override def sortService(mode: String): Future[Seq[Service]] = { repo.sortService(mode) }

  override def filterServiceById(id: Long): Future[Service] = { repo.filterServiceById(id) }

  // override def listAllService(sortingFields: Seq[(String, Boolean)], p: Int, s: Int): Future[Page[Service]] = ???

  override def filterServiceByName(name: String): Future[Seq[Service]] = { repo.filterServiceByName(name) }

  override def updateService(id: Long, service: Service): Future[Int] = ???
}
