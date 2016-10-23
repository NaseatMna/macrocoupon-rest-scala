package models.repositories

import com.google.inject.ImplementedBy
import models.Store
import models.repositories.impl.StoreRepo

import scala.concurrent.Future
/**
 * Created by Ky Sona on 10/5/2016.
 */
@ImplementedBy(classOf[StoreRepo])
trait IStoreRepo {
  def insertStore(store: Store): Future[Int]
  def updateStore(store: Store): Future[Int]
  def deleteStore(id: Long): Future[Int]
  def listAllStores(): Future[Seq[Store]]
  def findStoreById(id: Long): Future[Store]
  def findStoreByName(name: String): Future[Seq[Store]]
  def countStore(): Future[Int]

  // own define
  def listStores(): Future[Seq[Store]]
  def paginationStore(page: Int, limit: Int): Future[Seq[Store]]
  def sortStores(mode: String): Future[Seq[Store]]
}
