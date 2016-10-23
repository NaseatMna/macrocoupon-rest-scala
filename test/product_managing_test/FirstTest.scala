package product_managing_test

import models.manage_products.Product
import org.joda.time.DateTime
import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import product_managing_test.repositories.IproductTest
import product_managing_test.services.productService
import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by Oudam on 10/13/2016.
 */
class FirstTest extends FunSuite with BeforeAndAfter with MockitoSugar {

  val productRepo = mock[IproductTest]

  test("The sum function should equal 100") {
    val a = 10
    val b = 20

    when(productRepo.sum(a, b)) thenReturn a + b

    val service = new productService(productRepo)
    val actual = service.sum(a, b)
    // (4) verify the results
    assert(actual == 30)
  }

  test("List all products") {
    val data = Seq(new Product(Some(1), 1, 1, 1, "Car", "Luxerious Car", 100000.0, 1, Some(new DateTime()), "car.jpg"))
    //val mockData: Future[Seq[Product]] = Future(Seq(data))
    when(productRepo.list()) thenReturn data

    val service = new productService(productRepo)
    val actual = service.list()
    // (4) verify the results
    assert(actual == data)
    assert(actual == List(data))
  }

  test("Count all products") {
    val data = List(
      new Product(Some(1), 1, 1, 1, "Car", "Luxerious Car", 100000.0, 1, Some(new DateTime()), "car.jpg"),
      new Product(Some(2), 2, 2, 2, "Car", "Luxerious Car", 100000.0, 1, Some(new DateTime()), "car.jpg")
    )
    when(productRepo.count()) thenReturn Future(data.size)

    val service = new productService(productRepo)
    val actual = service.count()
    // (4) verify the results
    assert(Await.result(actual, 10 seconds) == 2)
  }

  test("Get product by its id") {
    val data = List(
      new Product(Some(1), 1, 1, 1, "Car", "Luxerious Car", 100000.0, 1, Some(new DateTime()), "car.jpg"),
      new Product(Some(2), 2, 2, 2, "Car", "Luxerious Car", 100000.0, 1, Some(new DateTime()), "car.jpg")
    )
    when(productRepo.findById(data.head.prod_id.get)) thenReturn Future(data.head)

    val service = new productService(productRepo)
    val actual = service.findById(1)
    // (4) verify the results
    assert(Await.result(actual, 10 seconds) == data.head)
  }

  test("Get product by its name") {
    val data = Seq(
      new Product(Some(1), 1, 1, 1, "Car", "Luxerious Car", 100000.0, 1, Some(new DateTime()), "car.jpg"),
      new Product(Some(2), 2, 2, 2, "Car", "Luxerious Car", 100000.0, 1, Some(new DateTime()), "car.jpg")
    )
    when(productRepo.findByName(data.head.prod_name)) thenReturn Future(data)

    val service = new productService(productRepo)
    //val no = service.findByName("car")
    val actual = service.findByName("Car")
    // (4) verify the results
    assert(Await.result(actual, 10 seconds) == data)
    //assert(Await.result(no,10 seconds) == null)
  }

  /*test("Test add new product"){
    val data = new Product(Some(1),1,1,1,"Car","Luxerious Car",100000.0,1,new Date(),"car.jpg")

    when(productRepo.insert(data)) thenReturn Future(1)

    val service = new productService(productRepo)
    val actual = service.insert(data)
    // (4) verify the results
    assert(Await.result(actual, 10 seconds) == 1)
  }

  test("Test add new product base one user and category") {
    val cate_name = "Smart Phone"
    val username = "Oudam"

    when(productRepo.getCategoryId(cate_name)) thenReturn Future(1)
    when(productRepo.getUserId(username)) thenReturn Future(1)

    val service = new productService(productRepo)
    val cate_id: Int = Await.result(service.getCategoryId(cate_name), 5 seconds)
    val user_id: Int = Await.result(service.getUserId(username), 10 seconds)
    val product = new Product(Some(1), user_id, cate_id, 1, "Car", "Luxerious Car", 100000.0, 1, new Date(), "car.jpg")
    val data = new Product(Some(1), 12, 12, 1, "Car", "Luxerious Car", 100000.0, 1, new Date(), "car.jpg")

    when(productRepo.insert(product)) thenReturn Future(1)
    val actual = service.insert(data)
    // (4) verify the results
    assert(cate_id == 1)
    assert(user_id == 1)

    assert(Await.result(actual,10 seconds) == 1)
  }*/

  test("Test update existed product") {
    val data = new Product(Some(1), 1, 1, 1, "Cars", "Luxerious Car", 100000.0, 1, Some(new DateTime()), "car.jpg")
    when(productRepo.update(data)) thenReturn Future(1)

    val service = new productService(productRepo)
    val actual = service.update(data)
    // (4) verify the results
    assert(Await.result(actual, 10 seconds) == 1)
  }

  test("Test delete existed product") {
    val id = 1;
    when(productRepo.delete(id)) thenReturn Future(1)

    val service = new productService(productRepo)
    val actual = service.delete(1)
    // (4) verify the results
    assert(Await.result(actual, 10 seconds) == 1)
  }

  test("Get category id by category_name") {
    val name = "Smart Phone";
    when(productRepo.getCategoryId(name)) thenReturn 1

    val service = new productService(productRepo)
    val actual = service.getCategoryId(name)
    // (4) verify the results

    assert(actual == 1)
  }

  test("Get user id by user_name") {
    val name = "Oudam";
    when(productRepo.getUserId(name)) thenReturn 1

    val service = new productService(productRepo)
    val actual = service.getUserId(name)
    // (4) verify the results

    assert(actual == 1)
    verify(productRepo.getUserId(name))
  }
}

