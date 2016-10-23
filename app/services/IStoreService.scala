package services

import api.Page
import com.google.inject.ImplementedBy
import models.Store
import services.impl.StoreService

import scala.concurrent.Future
/**
 * Created by Ky Sona on 10/5/2016.
 */
@ImplementedBy(classOf[StoreService])
trait IStoreService {
  def insertStore(store: Store): Future[Int]
  def updateStore(id: Long, store: Store): Future[Int]
  def deleteStore(id: Long): Future[Int]
  def listAllStores(sortingFields: Seq[(String, Boolean)], p: Int, s: Int): Future[Page[Store]]
  def findStoreById(id: Long): Future[Store]
  def findStoreByName(name: String): Future[Seq[Store]]
  def countStore(): Future[Int]

  // own define
  def listStores(): Future[Seq[Store]]
  def paginationStore(page: Int, limit: Int): Future[Seq[Store]]
  def sortStores(mode: String): Future[Seq[Store]]
}
