package controllers

import api.ApiError._
import com.google.inject._
import io.swagger.annotations._
import models.Service
import play.api.i18n.MessagesApi
import services.IServicesCategoryService

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by Naseat on 10/14/2016.
 */
@Api(value = "/Category Of Service")
class ServicesCategoryController @Inject() (services: IServicesCategoryService, val messagesApi: MessagesApi) extends api.ApiController {
  val sortingFields = Seq("servicecategory_id", "servicecategory_name")

  @ApiOperation(
    value = " add new service ",
    notes = " post method ",
    response = classOf[models.Service],
    httpMethod = "POST"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Data for add Services", required = true, dataType = "String", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 201, message = "Success"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def insertService() = SecuredApiActionWithBody /*Action.async(BodyParsers.parse.json)*/ { implicit request =>
    readFromRequest[Service] { service =>
      services.insertService(service).flatMap {
        case 1 => created("service have been inserted : " + 1)
        case _ => errorBadRequest("Service could not be inserted.")
      }.fallbackTo(errorInternal)
    }
  }

  /*
      filter service by it  id
   */

  @ApiOperation(
    value = " get service by id ",
    notes = " return exist service by id ",
    response = classOf[models.Service],
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error.")

  ))
  def getServiceByID(id: Long) = SecuredApiAction { implicit request =>
    services.filterServiceById(id).flatMap {
      case service => ok(service)
      case null => errorNotFound
    }.fallbackTo(errorInternal)
  }
  /*
    get exist service by service Name
   */
  @ApiOperation(
    value = "get service by name",
    notes = " return exist service name ",
    response = classOf[models.Service],
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error.")
  ))
  def getServiceByName(name: String) = SecuredApiAction { implicit request =>
    services.filterServiceByName(name).flatMap {
      case service => ok(service)
      case null => errorNotFound
    }.fallbackTo(errorInternal)
  }
  /*
    Get All list of Service
   */
  @ApiOperation(
    value = "get all list of service",
    notes = "return all list service category",
    response = classOf[models.Service],
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "OK"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal Server Error.")
  ))
  def getListAllService(sort: Option[String], p: Int, s: Int) = SecuredApiAction { implicit request =>
    sortedPage(sort, sortingFields, default = "servicecategory_id") { sortingFields =>
      services.listAllService(sortingFields, p, s)
    }.fallbackTo(errorInternal)
  }
  /*
    Update an exist Service by service id
 */
  @ApiOperation(
    value = "Update Service ",
    notes = "update service ",
    response = classOf[models.Service],
    httpMethod = "PUT"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Data for update Service", required = true, dataType = "String", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Succes"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal Server Error.")
  ))
  def updateServiceByID(id: Long) = SecuredApiActionWithBody { implicit request =>
    readFromRequest[Service] { service =>
      services.updateService(id, service).flatMap {
        case 1 => ok("Category has been successful updated : " + 1)
        case _ => errorBadRequest("Category could not be updated.")
      }.fallbackTo(errorInternal)
    }
  }
  /*
      delete service by service id
   */
  @ApiOperation(
    value = "Delete Service",
    notes = "delete exist service by id",
    httpMethod = "DELETE"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Success"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def deleteServiceCategoryByID(id: Long) = SecuredApiAction {
    implicit request =>
      services.deleteService(id).flatMap {

        case 1 => ok("Service has been deleted : " + 1)
        case _ => errorBadRequest("Can't delete service")
      }.fallbackTo(errorInternal)
  }
  /*
    Sort Service by name
   */
  @ApiOperation(
    value = "Sort service",
    notes = "Returns all exist service with sorting",
    response = classOf[models.Service],
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error.")
  ))
  def sortService(mode: String) = SecuredApiAction { implicit request =>
    services.sortService(mode).flatMap { // flatMap case data from future reactive
      case service => ok(service) // in ok functions return json
      case null => errorNotFound
    }.fallbackTo(errorInternal)
  }
}
