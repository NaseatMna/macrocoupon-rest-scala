package product_managing_test

import java.util.Date

import models.manage_products.Product
import org.joda.time.DateTime
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.AsyncFunSuite
import play.api.libs.json.Json
import play.api.test.FakeRequest
import product_managing_test.controllers.TestController
import play.api.mvc._
import play.api.test.Helpers._
import product_managing_test.repositories.IproductTest

import product_managing_test.services.IproductService
//import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by Oudam on 10/14/2016.
 */

class ControllerTesting extends AsyncFunSuite with MockitoSugar with Results {

  val mockService = mock[IproductService]
  val productRepo = mock[IproductTest]
  val controller = new TestController(mockService)

  /*test("Test index"){
    val controller = new TestController(mockService)
    val result: Future[Result] = controller.index().apply(FakeRequest())
    val bodyText: String = contentAsString(result)
    assert(bodyText == "Test!")
  }

  test("Test add new product") {
    val controller = new TestController(mockService)
  test("Test valSum"){
    when(productRepo.sum(10,20)) thenReturn 30
    val serv = new productService(productRepo)
    val controller = new TestController(serv)
    val result: Future[Result] = controller.valSum().apply(FakeRequest())
    val bodyText: String = contentAsString(result)
    assert(status(result) == 200)
    assert(bodyText == "30")
  }*/

  test("Test get user id") {
    val name = "Oudam"
    when(mockService.getUserId(any[String])) thenReturn 1

    val result = controller.getUserId(name).apply(FakeRequest().withHeaders(CONTENT_TYPE -> "application/json"))
    assert(status(result) == 200)
    assert(contentAsJson(result) == Json.toJson(1))
  }

  test("Test get category id") {
    val name = "Smart Phone"
    when(mockService.getCategoryId(any[String])) thenReturn 1

    val result = controller.getCategoryId(name).apply(FakeRequest().withHeaders(CONTENT_TYPE -> "application/json"))
    assert(status(result) == 200)
    assert(contentAsJson(result) == Json.toJson(1))
  }

  test("Test get all product") {
    val mockData = Seq(new Product(Some(1), 1, 1, 1, "Cars", "Luxerious Car", 100000.0, 1, Some(new DateTime()), "car.jpg"))
    when(mockService.list()) thenReturn mockData

    val result = controller.listProducts().apply(FakeRequest().withHeaders(CONTENT_TYPE -> "application/json"))
    assert(status(result) == 200)
    assert(contentAsJson(result) == Json.toJson(mockData))
  }

  test("Test add new product") {
    when(mockService.insert(any[Product])) thenReturn 1
    val request = FakeRequest().withBody(Json.obj(
      "prod_id" -> 1,
      "user_id" -> 1,
      "prod_cate_id" -> 1,
      "prod_seo_id" -> 1,
      "prod_name" -> "Oudam",
      "prod_description" -> "kaka",
      "prod_price" -> 1.1,
      "isPublished" -> 1,
      "createdDate" -> new Date(),
      "prod_image" -> "image.jpg"
    ))
    val result: Future[Result] = controller.insertProduct().apply(request.withHeaders(CONTENT_TYPE -> "application/json"))
    val bodyText: String = contentAsString(result)
    assert(status(result) == 201)
    assert(bodyText == "Product has been inserted : 1")
  }

  test("handleJsonRequestss") {
    val mockService = mock[IproductService]
    val controller = new TestController(mockService)
    val request = FakeRequest().withBody(Json.obj(
      "prod_cate_id" -> 1,
      "prod_cate_name" -> "Oudam",
      "prod_cate_description" -> "Oudam"
    ))

    val result: Future[Result] = controller.handleJsonRequest().apply(request)
    val bodyText: String = contentAsString(result)
    assert(bodyText == "Product have been inserted.")
  }
}
