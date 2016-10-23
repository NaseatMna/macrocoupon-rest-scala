package models.repositories

import com.google.inject.ImplementedBy
import models.Category
import models.repositories.impl.ProductCategoryRepo

import scala.concurrent.Future

/**
 * Created by Oudam on 9/20/2016.
 */
@ImplementedBy(classOf[ProductCategoryRepo])
trait IProductCategoryRepo {
  def insert(category: Category): Future[Int]
  def update(category: Category): Future[Int]
  def delete(id: Long): Future[Int]
  def list(): Future[Seq[Category]]
  def findById(id: Long): Future[Category]
  def findByName(name: String): Future[Seq[Category]]
  def count(): Future[Int]
}
