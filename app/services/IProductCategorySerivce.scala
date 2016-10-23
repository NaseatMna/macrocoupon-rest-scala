package services

import api.Page
import com.google.inject.ImplementedBy
import models.{ Category }
import services.impl.ProductCategoryService

import scala.concurrent.Future

/**
 * Created by Oudam on 9/22/2016.
 */
@ImplementedBy(classOf[ProductCategoryService])
trait IProductCategorySerivce {
  def insert(category: Category): Future[Int]
  def update(id: Long, category: Category): Future[Int]
  def delete(id: Long): Future[Int]
  def list(sortingFields: Seq[(String, Boolean)], p: Int, s: Int): Future[Page[Category]]
  def findById(id: Long): Future[Category]
  def findByName(name: String): Future[Seq[Category]]
  def count(): Future[Int]
}
