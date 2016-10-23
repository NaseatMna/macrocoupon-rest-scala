package models.repositories.impl

import com.google.inject.Inject
import models.repositories.IStoreRepo
import models.{ Store, TableStore }
import play.api.db.slick.{ HasDatabaseConfig, DatabaseConfigProvider }
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import scala.concurrent.{ Await, Future }

/**
 * Created by Ky Sona on 10/5/2016.
 */
class StoreRepo @Inject() (dbConfigProvider: DatabaseConfigProvider) extends IStoreRepo with HasDatabaseConfig[JdbcProfile] {
  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val stores = TableQuery[TableStore]
  /*
    select row which its id equal id given
   */
  private def filterQuery(id: Long): Query[TableStore, Store, Seq] = {
    stores.filter(_.id === id)
  }

  /*
    insert store to table store
   */
  override def insertStore(store: Store): Future[Int] = {
    try dbConfig.db.run(stores += store)
    //finally db.close
  }

  /*
    count amount of rows in table store
   */
  override def countStore(): Future[Int] = {
    try dbConfig.db.run(stores.length.result)
    //finally db.close
  }

  /*
    get all stores from table store
   */
  override def listAllStores(): Future[Seq[Store]] = {
    val query =
      for {
        stores <- stores if stores != null
      } yield stores
    try dbConfig.db.run(query.result)
  }

  /*
    delete store which its id equal id given
   */
  override def deleteStore(id: Long): Future[Int] = {
    try dbConfig.db.run(filterQuery(id).delete)
    //finally db.close
  }

  /*
    get stores by its name
   */
  override def findStoreByName(name: String): Future[Seq[Store]] = {
    try dbConfig.db.run(stores.filter(_.name.toLowerCase like ("%" ++ name ++ "%").toLowerCase).result)
    //finally db.close
  }

  /*
    update store by store id
   */
  override def updateStore(store: Store): Future[Int] = {
    try dbConfig.db.run(filterQuery(store.store_id.get).update(store))
    //finally db.close
  }
  /*
    get store by its id
   */
  override def findStoreById(id: Long): Future[Store] = {
    try dbConfig.db.run(filterQuery(id).result.head)
    //finally db.close
  }

  //override protected val dbConfig: DatabaseConfig[JdbcProfile] = _
  // own define
  override def listStores(): Future[Seq[Store]] = {
    val query = for (stores <- stores) yield stores
    try dbConfig.db.run(query.result);
  }

  override def paginationStore(page: Int, limit: Int): Future[Seq[Store]] = {
    val start = (page * limit) - limit
    val query = stores.drop(start).take(limit)
    try dbConfig.db.run(query.result);
  }

  override def sortStores(mode: String): Future[Seq[Store]] = {
    if (mode == "asc") { // sort with descending
      val query = stores.sortBy(_.name.asc.nullsFirst)
      try dbConfig.db.run(query.result);
    } else { // sort with ascending
      val query = stores.sortBy(_.name.desc.nullsFirst)
      try dbConfig.db.run(query.result);
    }
  }

}
