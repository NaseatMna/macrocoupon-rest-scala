package controllers

import com.google.inject.Inject
import models.manage_products.Product
import play.api.i18n.MessagesApi
import play.api.libs.json.{ JsValue, Json }
import play.api.mvc.{ BodyParsers, Action }
import services.managing_products.IProductService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ TimeoutException, Future }

/**
 * Created by Oudam on 10/17/2016.
 */
class ProductController @Inject() (productService: IProductService, val messagesApi: MessagesApi) extends api.ApiController {

  def insertProduct() = Action.async(BodyParsers.parse.json) { implicit request =>
    {
      /*    readFromRequest[Product] { product =>
        productService.insertNewProduct(product).flatMap {
          case 1 => created("Product have been inserted : " + 1)
          case _ => errorBadRequest("Product could not be inserted.")
        }.fallbackTo(errorInternal)
      }

    }*/
      val jsonBody: JsValue = request.body
      //converting json to case class
      val product: Product = jsonBody.as[Product]
      /*Product(Some(0), product.user_id, product.prod_cate_id, product.prod_seo_id, product.prod_name,
        product.prod_description, product.prod_price, product.isPublished, product.createdDate, product.prod_image)*/
      productService.insertNewProduct(product)
        .map(result => Created("Product has been inserted : " + result))
        .recoverWith {
          case e: Throwable => Future(BadRequest("Wrong json format"))
        }.fallbackTo(Future(InternalServerError("There was an error at the server")))
    }
  }

  def listProducts() = Action.async { implicit request =>
    productService.list().map(employee => Ok(Json.toJson(employee))).recover {
      case ex: TimeoutException => InternalServerError(ex.getMessage)
    }
  }
}

