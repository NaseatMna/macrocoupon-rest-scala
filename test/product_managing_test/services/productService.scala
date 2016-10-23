package product_managing_test.services

import com.google.inject.Inject
import models.manage_products.{ Category, Product }
import product_managing_test.repositories.IproductTest

import scala.concurrent.Future

/**
 * Created by Oudam on 10/13/2016.
 */
class productService @Inject() (repo: IproductTest) extends IproductService {

  override def list(): Seq[Product] = repo.list()

  override def insert(product: Product): Int = repo.insert(product)

  override def count(): Future[Int] = repo.count()

  override def update(product: Product): Future[Int] = repo.update(product)

  override def findByName(name: String): Future[Seq[Product]] = repo.findByName(name)

  override def findById(id: Long): Future[Product] = repo.findById(id)

  override def delete(id: Long): Future[Int] = repo.delete(id)

  override def getCategoryId(name: String): Int = repo.getCategoryId(name)

  override def addNewCategory(category: Category): Future[Int] = repo.addNewCategory(category)

  override def getLastCategoryId(): Future[Int] = repo.getLastCategoryId()

  override def getUserId(email: String): Int = repo.getUserId(email)

  override def sum(x: Int, y: Int): Int = repo.sum(x, y)
}
