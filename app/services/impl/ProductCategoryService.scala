package services.impl

import api.Api.Sorting._
import api.Page
import com.google.inject.Inject
import models.Category
import models.repositories.IProductCategoryRepo
import services.IProductCategorySerivce

import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._

/**
 * Created by Oudam on 9/22/2016.
 */
class ProductCategoryService @Inject() (repo: IProductCategoryRepo) extends IProductCategorySerivce {

  override def insert(category: Category): Future[Int] = repo.insert(category)

  override def count(): Future[Int] = repo.count()

  override def update(id: Long, category: Category): Future[Int] = {
    val data = Category(Option(id), category.category_name, category.category_description, category.category_image)
    repo.update(data)
  }

  override def findById(id: Long): Future[Category] = repo.findById(id)

  override def findByName(name: String): Future[Seq[Category]] = repo.findByName(name)

  override def delete(id: Long): Future[Int] = repo.delete(id)

  override def list(sortingFields: Seq[(String, Boolean)], p: Int, s: Int): Future[Page[Category]] = Future.successful {
    pages(p, s)(sortingFields.map(sortingFunc): _*)
  }

  def pages(p: Int, s: Int)(sortFuncs: ((Category, Category) => Boolean)*): Page[Category] = {
    val sorted = sortFuncs.foldRight(Await.result(repo.list(), 10 seconds).toList)((f, items) => items.sortWith(f))
    Page(
      //items = sorted.drop((p - 1) * s).take(s),
      items = sorted.slice((p - 1) * s, ((p - 1) * s) + s),
      page = p,
      size = s,
      total = sorted.size
    )
  }

  def sortingFunc(fieldsWithOrder: (String, Boolean)): (Category, Category) => Boolean = fieldsWithOrder match {
    case ("category_id", ASC) => _.category_id.get < _.category_id.get
    case ("category_id", DESC) => _.category_id.get > _.category_id.get
    case ("category_name", ASC) => (a, b) => (a.category_name compareTo b.category_name) < 0
    case ("category_name", DESC) => (a, b) => (a.category_name compareTo b.category_name) > 0
    case _ => (_, _) => false
  }
}
