package product_managing_test.controllers

import com.google.inject.Inject
import models.manage_products.{ Category, Product }
import play.api.libs.json.{ Json, JsValue }
import play.api.mvc.{ BodyParsers, Action, Controller }
import product_managing_test.services.IproductService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ TimeoutException, Await, Future }
import scala.concurrent.duration._

/**
 * Created by Oudam on 10/14/2016.
 */
class TestController @Inject() (prodService: IproductService) extends Controller {

  def index() = Action { implicit request =>
    Ok("Test!")
  }

  def valSum() = Action { implicit request =>
    val result: Int = prodService.sum(10, 20)
    Ok(Json.toJson(result))
  }

  def insertProduct() = Action.async(BodyParsers.parse.json) { implicit request =>
    {
      val jsonBody: JsValue = request.body
      //converting json to case class
      val product: Product = jsonBody.as[Product]
      println(s"case class $product")

      Future(prodService.insert(product)).map(result => Created("Product has been inserted : " + result)).recoverWith {
        case _: Throwable =>
          Future(BadRequest("Wrong json format"))
          Future(prodService.insert(product)).map(result => Created("Product has been inserted : " + result)).recoverWith {
            case e: Throwable => Future(BadRequest("Wrong json format"))
          }.fallbackTo(Future(InternalServerError("There was an error at the server")))
      }
    }
  }

  def listProducts() = Action.async { implicit request =>
    Future(prodService.list()).map(employee => Ok(Json.toJson(employee))).recover {
      case ex: TimeoutException =>
        InternalServerError(ex.getMessage)
    }.fallbackTo(Future(InternalServerError("There was an error at the server")))
  }

  def getCategoryId(name: String) = Action.async { implicit request =>
    {
      Future(prodService.getCategoryId(name)).map(result => Ok(Json.toJson(result))).recoverWith {
        case e: Throwable => Future(BadRequest("Wrong json format"))
      }.fallbackTo(Future(InternalServerError("There was an error at the server")))
    }
  }

  def getUserId(name: String) = Action.async { implicit request =>
    {
      Future(prodService.getUserId(name)).map(result => Ok(Json.toJson(result))).recoverWith {
        case e: Throwable => Future(BadRequest("Wrong json format"))
      }.fallbackTo(Future(InternalServerError("There was an error at the server")))
    }
  }

  def handleJsonRequest = Action(BodyParsers.parse.json) { request =>
    //using the body parser to parse request body to json
    val jsonBody: JsValue = request.body
    //converting json to case class
    val category: Category = jsonBody.as[Category]
    println(s"case class $category")
    //making some modification to case class
    val anotherCate = category.copy(prod_cate_name = "This is a new value")
    //converting back to json
    val anotherJson: JsValue = Json.toJson(anotherCate)
    //sending the json response
    Ok(anotherJson)
  }
}
