package controllers

import api.ApiError._
import com.google.inject._
import io.swagger.annotations._
import models._
import services.IProductCategorySerivce
import play.api.i18n.{ MessagesApi }
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by Oudam on 9/20/2016.
 */

@Api(value = "/Category Of Product")
class ProductCategoryController @Inject() (service: IProductCategorySerivce, val messagesApi: MessagesApi) extends api.ApiController {

  val sortingFields = Seq("category_id", "category_name")
  /**
   * Add new categories
   *
   * @return number rows inserted
   */
  @ApiOperation(
    value = "Add new category",
    notes = "Post Method",
    response = classOf[models.Category],
    httpMethod = "POST"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Data for add category", required = true, dataType = "String", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 201, message = "Success"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def insertNewCategory() = SecuredApiActionWithBody /*Action.async(BodyParsers.parse.json)*/ { implicit request =>
    readFromRequest[Category] { category =>
      service.insert(category).flatMap {
        case 1 => created("Category have been inserted : " + 1)
        case _ => errorBadRequest("Category could not be inserted.")
      }.fallbackTo(errorInternal)
    }

    /*{
      for {
        id <- (request.body \ "category_id").asOpt[Long]
        name <- (request.body \ "category_name").asOpt[String]
        description <- (request.body \ "category_description").asOpt[String]
        image <- (request.body \ "category_image").asOpt[String]
      } yield {
        (service.insert(Category(id, name, description, image)) map { n => Created("Categories have been inserted : " + n) }).recoverWith {
          case e => Future {
            InternalServerError("There was an error at the server")
          }
        }
      }
    }.getOrElse(Future { BadRequest("Wrong json format") })*/
  }

  /**
   * Get an exist category by id
   *
   * @param id
   * @return json of category
   */
  @ApiOperation(
    value = "Get category by id",
    notes = "Returns exist category by id",
    response = classOf[models.Category],
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error.")
  ))
  def listCategoryById(id: Long) = SecuredApiAction { implicit request =>
    /*service.findById(id).map(employee => Ok(Json.toJson(employee))).recover {
      case ex: TimeoutException =>
        Logger.error("Problem found in employee edit process")
        InternalServerError(ex.getMessage)
    }*/
    service.findById(id).flatMap {
      case employee => ok(employee)
      case null => errorNotFound
    }.fallbackTo(errorInternal)
  }

  /**
   * Get all exist categories by its name
   *
   * @param name
   * @return json of category
   */
  @ApiOperation(
    value = "Get categories by name",
    notes = "Returns exist categories by name",
    response = classOf[models.Category],
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error.")
  ))
  def listCategoryByName(name: String) = SecuredApiAction { implicit request =>
    /*service.findById(id).map(employee => Ok(Json.toJson(employee))).recover {
      case ex: TimeoutException =>
        Logger.error("Problem found in employee edit process")
        InternalServerError(ex.getMessage)
    }*/
    service.findByName(name).flatMap {
      case employee => ok(employee)
      case null => errorNotFound
    }.fallbackTo(errorInternal)
  }

  /**
   * List all exist category
   *
   * @return json list of categories
   */
  @ApiOperation(
    value = "Get all categories",
    notes = "Returns list of all categories",
    response = classOf[models.Category],
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error.")
  ))
  def listAllCategory(sort: Option[String], p: Int, s: Int) = SecuredApiAction { implicit request =>
    sortedPage(sort, sortingFields, default = "category_id") { sortingFields =>
      service.list(sortingFields, p, s)
    }.fallbackTo(errorInternal)
  }

  /**
   * Update an exist category by id
   *
   * @param id
   * @return number 1 if success
   */
  @ApiOperation(
    value = "Update category ",
    notes = "update exist category by its id",
    response = classOf[models.Category],
    httpMethod = "PUT"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Data for update category", required = true, dataType = "String", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Success"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def updateCategoryById(id: Long) = SecuredApiActionWithBody /*Action.async(BodyParsers.parse.json)*/ { implicit request =>
    readFromRequest[Category] { category =>
      service.update(id, category).flatMap {
        case 1 => ok("Category has been successful updated : " + 1)
        case _ => errorBadRequest("Category could not be updated.")
      }.fallbackTo(errorInternal)
    }
    /*{
      for {
        name <- (request.body \ "category_name").asOpt[String]
        description <- (request.body \ "category_description").asOpt[String]
        image <- (request.body \ "category_image").asOpt[String]
      } yield {
        (service.update(id, Category(id, name, description, image)) map { n => Created("Categories have been updated : " + n) }).recoverWith {
          case e => Future {
            InternalServerError("There was an error at the server")
          }
        }
      }
    }.getOrElse(Future { BadRequest("Wrong json format") })*/
  }

  /**
   * Delete an exist category by id
   *
   * @param id
   * @return number 1 if success
   */
  @ApiOperation(
    value = "Delete category",
    notes = "delete exist category by its id",
    httpMethod = "DELETE"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Success"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def deleteCategoryById(id: Long) = SecuredApiAction { implicit request =>
    /* service.delete(id).map { n => Ok("Category have been deleted : " + n) }.recover {
      case ex: TimeoutException =>
        Logger.error("Problem found in category delete process")
        InternalServerError(ex.getMessage)
    }*/
    service.delete(id).flatMap {
      case 1 => ok("Category has been successful deleted : " + 1)
      case _ => errorBadRequest("Category could not be deleted.")
    }.fallbackTo(errorInternal)
  }
}
