package services.impl

import api.Api.Sorting._
import api.Page
import com.google.inject.Inject
import models.Store
import models.repositories.IStoreRepo
import services.IStoreService

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._

/**
 * Created by Ky Sona on 10/5/2016.
 */
class StoreService @Inject() (repoStore: IStoreRepo) extends IStoreService {
  /*
    add store
   */
  override def insertStore(store: Store): Future[Int] = repoStore.insertStore(store)

  /*
    list all stores by pagination and sorting
   */
  override def listAllStores(sortingFields: Seq[(String, Boolean)], p: Int, s: Int): Future[Page[Store]] = Future.successful {
    pages(p, s)(sortingFields.map(sortingFunc): _*)
  }

  override def countStore(): Future[Int] = repoStore.countStore()

  override def deleteStore(id: Long): Future[Int] = repoStore.deleteStore(id)

  override def findStoreByName(name: String): Future[Seq[Store]] = repoStore.findStoreByName(name)

  override def updateStore(id: Long, store: Store): Future[Int] = {
    val data = Store(Option(id), store.store_name)
    repoStore.updateStore(data)
  }

  override def findStoreById(id: Long): Future[Store] = repoStore.findStoreById(id)

  // Helper methods
  def pages(p: Int, s: Int)(sortFuncs: ((Store, Store) => Boolean)*): Page[Store] = {
    val sorted = sortFuncs.foldRight(Await.result(repoStore.listAllStores(), 10 seconds).toList)((f, items) => items.sortWith(f))
    Page(
      items = sorted.slice((p - 1) * s, ((p - 1) * s) + s),
      page = p,
      size = s,
      total = sorted.size
    )
  }

  def sortingFunc(fieldsWithOrder: (String, Boolean)): (Store, Store) => Boolean = fieldsWithOrder match {
    case ("store_id", ASC) => _.store_id.get < _.store_id.get
    case ("store_id", DESC) => _.store_id.get > _.store_id.get
    case ("store_name", ASC) => (a, b) => (a.store_name compareTo b.store_name) < 0
    case ("store_name", DESC) => (a, b) => (a.store_name compareTo b.store_name) > 0
    case _ => (_, _) => false
  }

  // own define
  override def listStores(): Future[Seq[Store]] = repoStore.listStores()

  override def paginationStore(page: Int, limit: Int): Future[Seq[Store]] = repoStore.paginationStore(page, limit)

  override def sortStores(mode: String): Future[Seq[Store]] = repoStore.sortStores(mode)

}
