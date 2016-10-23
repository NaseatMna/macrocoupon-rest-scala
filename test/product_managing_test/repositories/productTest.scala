package product_managing_test.repositories

import com.google.inject.Inject
import models.manage_products._
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfig }
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._

/**
 * Created by Oudam on 10/13/2016.
 */
class productTest @Inject() (dbConfigProvider: DatabaseConfigProvider) extends IproductTest with HasDatabaseConfig[JdbcProfile] {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val products = TableQuery[TProduct]
  val categories = TableQuery[TCategory]
  val users = TableQuery[TUser]

  /**
   * Filter product-category with id
   */
  private def filterQuery(id: Long): Query[TProduct, Product, Seq] = {
    products.filter(_.id === id)
  }

  override def list(): Seq[Product] = {
    val query =
      for {
        product <- products if product != null
      } yield product
    try Await.result(dbConfig.db.run(query.result), 10.seconds)
  }

  override def insert(product: Product): Int = {
    try {
      Await.result(dbConfig.db.run(products += product), 10.seconds)
    } catch {
      case _: Throwable => 0
    }
  }

  override def count(): Future[Int] = dbConfig.db.run(products.length.result)

  override def update(product: Product): Future[Int] = dbConfig.db.run(filterQuery(product.prod_id.get).update(product))

  override def findByName(name: String): Future[Seq[Product]] = dbConfig.db.run(products.filter(_.name.toLowerCase like ("%" ++ name ++ "%").toLowerCase).result)

  override def findById(id: Long): Future[Product] = dbConfig.db.run(filterQuery(id).result.head)

  override def delete(id: Long): Future[Int] = dbConfig.db.run(filterQuery(id).delete)

  override def getCategoryId(name: String): Int = Await.result(dbConfig.db.run(categories.filter(_.name === name).result.head).map(_.prod_cate_id.get), 10.seconds)

  override def addNewCategory(category: Category): Future[Int] = dbConfig.db.run(categories += category)

  override def getUserId(email: String): Int = Await.result(dbConfig.db.run(users.filter(_.username === email).result.head).map(_.user_id.get), 10.seconds)

  override def getLastCategoryId(): Future[Int] = Future(1)

  override def sum(x: Int, y: Int): Int = { x + y }

}
