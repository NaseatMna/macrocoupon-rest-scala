package models.manage_products.repositories

import com.google.inject.ImplementedBy
import models.manage_products.repositories.impl.ProductRepo

import scala.concurrent.Future
import models.manage_products._
/**
 * Created by Oudam on 10/14/2016.
 */
@ImplementedBy(classOf[ProductRepo])
trait IProductRepo {
  def insertNewProduct(product: Product): Future[Int]
  def getCategoryId(name: String): Future[Int]
  def getUserId(email: String): Future[Int]
  def list(): Future[Seq[Product]]
}
