package services

/**
 * Created by Naseat on 10/14/2016.
 */
import api.Page
import com.google.inject.ImplementedBy
import models.Service
import services.impl.SevicesCategoryService
import scala.concurrent.Future
@ImplementedBy(classOf[SevicesCategoryService])
trait IServicesCategoryService {
  def insertService(service: Service): Future[Int]
  def updateService(id: Long, service: Service): Future[Int]
  def deleteService(id: Long): Future[Int]
  def listAllService(sortingFields: Seq[(String, Boolean)], p: Int, s: Int): Future[Page[Service]]
  def filterServiceById(id: Long): Future[Service]
  def filterServiceByName(name: String): Future[Seq[Service]]
  def countService(): Future[Int]
  def sortService(mode: String): Future[Seq[Service]]
  //def listServiceWithSort(sort : String,p: Int,s : Int):Future[Seq[Service]]
}
