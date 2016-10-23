package models.repositories.impl

import com.google.inject.Inject
import models.repositories.IProductCategoryRepo
import models.{ Category, ProductCategory }
import play.api.db.slick.{ HasDatabaseConfig, DatabaseConfigProvider }
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._

import scala.concurrent.{ Await, Future }

/**
 * Created by Oudam on 9/20/2016.
 */
class ProductCategoryRepo @Inject() (dbConfigProvider: DatabaseConfigProvider) extends IProductCategoryRepo with HasDatabaseConfig[JdbcProfile] {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val categories = TableQuery[ProductCategory]

  /**
   * Filter product-category with id
   */
  private def filterQuery(id: Long): Query[ProductCategory, Category, Seq] = {
    categories.filter(_.id === id)
  }

  /**
   * Create a new product-category
   */
  override def insert(category: Category): Future[Int] = {
    try dbConfig.db.run(categories += category)
    //finally db.close
  }

  /**
   * Count total product-category in database
   */

  override def count(): Future[Int] = {
    try dbConfig.db.run(categories.length.result)
    //finally db.close
  }

  /**
   * Update product-category with id
   */
  override def update(category: Category): Future[Int] = {
    try dbConfig.db.run(filterQuery(category.category_id.get).update(category))
    //finally db.close
  }

  /**
   * Find product-category by id
   */
  override def findById(id: Long): Future[Category] = {
    try dbConfig.db.run(filterQuery(id).result.head)
    //finally db.close
  }

  /**
   * Delete product-category with id
   */
  override def delete(id: Long): Future[Int] = {
    try dbConfig.db.run(filterQuery(id).delete)
    //finally db.close
  }

  /**
   * Return a list of product-categories
   */
  override def list(): Future[Seq[Category]] = {
    val query =
      for {
        category <- categories if category != null
      } yield category
    try dbConfig.db.run(query.result)
  }

  override def findByName(name: String): Future[Seq[Category]] = {
    try dbConfig.db.run(categories.filter(_.name.toLowerCase like ("%" ++ name ++ "%").toLowerCase).result)
  }
}
