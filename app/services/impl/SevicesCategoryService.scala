package services.impl

import api.Api.Sorting._
import api.Page
import com.google.inject.Inject
import models.Service
import models.repositories.IServicesRepo
import services.IServicesCategoryService

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
/**
 * Created by Naseat on 10/14/2016.
 */
class SevicesCategoryService @Inject() (repoService: IServicesRepo) extends IServicesCategoryService {

  override def insertService(service: Service): Future[Int] = repoService.insertService(service)

  override def deleteService(id: Long): Future[Int] = repoService.deleteService(id)

  override def countService(): Future[Int] = repoService.countService()

  override def filterServiceById(id: Long): Future[Service] = repoService.filterServiceById(id)

  override def filterServiceByName(name: String): Future[Seq[Service]] = repoService.filterServiceByName(name)

  override def listAllService(sortingFields: Seq[(String, Boolean)], p: Int, s: Int): Future[Page[Service]] = Future.successful {
    pages(p, s)(sortingFields.map(sortingFunc): _*)
  }

  def pages(p: Int, s: Int)(sortFuncs: ((Service, Service) => Boolean)*): Page[Service] = {
    val sorted = sortFuncs.foldRight(Await.result(repoService.listAllService(), 10 seconds).toList)((f, items) => items.sortWith(f))
    Page(
      items = sorted.slice((p - 1) * s, ((p - 1) * s) + s),
      page = p,
      size = s,
      total = sorted.size
    )
  }
  def sortingFunc(fieldsWithOrder: (String, Boolean)): (Service, Service) => Boolean = fieldsWithOrder match {
    case ("servicecategory_id", ASC) => _.servicecategory_id.get < _.servicecategory_id.get
    case ("servicecategory_id", DESC) => _.servicecategory_id.get > _.servicecategory_id.get
    case ("servicecategory_name", ASC) => (a, b) => (a.servicecategory_name compareTo b.servicecategory_name) < 0
    case ("servicecategory_name", DESC) => (a, b) => (a.servicecategory_name compareTo b.servicecategory_name) > 0
    case _ => (_, _) => false
  }

  override def updateService(id: Long, service: Service): Future[Int] = {
    val data = Service(Option(id), service.servicecategory_name, service.servicecategory_description)
    repoService.updateService(data)
  }
  override def sortService(mode: String): Future[Seq[Service]] = repoService.sortService(mode)
  //  override def listServiceWithSort(sort : String, p: Int, s : Int):Future[Seq[Service]]= Future.successful{
  //    pages(p,s) repoService.listServiceWithSort(sort)
  //  }
}
