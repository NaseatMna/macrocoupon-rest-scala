package controllers

import api.ApiError._
import com.google.inject._
import io.swagger.annotations._
import models.Store
import services.IStoreService
import play.api.i18n.{ MessagesApi }
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by Ky Sona on 10/5/2016.
 */
@Api(value = "/Store")
class StoreController @Inject() (storeService: IStoreService, val messagesApi: MessagesApi) extends api.ApiController {
  val sortingFields = Seq("store_id", "store_name")

  /**
   * Add new store
   *
   * @return number rows inserted
   */
  @ApiOperation(
    value = "Add new store",
    notes = "Post Method",
    response = classOf[models.Store],
    httpMethod = "POST"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Data for add store", required = true, dataType = "String", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 201, message = "Success"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def insertStore() = SecuredApiActionWithBody /*Action.async(BodyParsers.parse.json)*/ { implicit request =>
    readFromRequest[Store] { store =>
      storeService.insertStore(store).flatMap {
        case 1 => created("Store have been inserted : " + 1)
        case _ => errorBadRequest("Store could not be inserted.")
      }.fallbackTo(errorInternal)
    }
  }

  /**
   * Get an exist store by id
   *
   * @param id
   * @return json of store
   */
  @ApiOperation(
    value = "Get store by id",
    notes = "Returns exist store by id",
    response = classOf[models.Store],
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error.")
  ))
  def getStoreById(id: Long) = SecuredApiAction { implicit request =>
    storeService.findStoreById(id).flatMap {
      case store => ok(store)
      case null => errorNotFound
    }.fallbackTo(errorInternal)
  }

  /**
   * Filter stores by name
   *
   * @param name
   * @return json of store
   */
  @ApiOperation(
    value = "Filter stores by name",
    notes = "Returns exist store by name",
    response = classOf[models.Store],
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error.")
  ))
  def listStoreByName(name: String) = SecuredApiAction { implicit request =>
    storeService.findStoreByName(name).flatMap {
      case store => ok(store)
      case null => errorNotFound
    }.fallbackTo(errorInternal)
  }

  /**
   * List all exist stores with pagination and sorting
   *
   * @return json list of stores
   */
  @ApiOperation(
    value = "Get all stores",
    notes = "Returns all exist stores by pagination and sorting",
    response = classOf[models.Store],
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error.")
  ))
  def listAllStores(sort: Option[String], p: Int, s: Int) = SecuredApiAction { implicit request =>
    sortedPage(sort, sortingFields, default = "store_id") { sortingFields =>
      storeService.listAllStores(sortingFields, p, s)
    }.fallbackTo(errorInternal)
  }

  /**
   * Update an exist store by id
   *
   * @param id
   * @return number 1 if success
   */
  @ApiOperation(
    value = "Update store ",
    notes = "update exist store by its id",
    response = classOf[models.Store],
    httpMethod = "PUT"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Data for update store", required = true, dataType = "String", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Success"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def updateStoreById(id: Long) = SecuredApiActionWithBody /*Action.async(BodyParsers.parse.json)*/ { implicit request =>
    readFromRequest[Store] { store =>
      storeService.updateStore(id, store).flatMap {
        case 1 => ok("Store has been successful updated : " + 1)
        case _ => errorBadRequest("Store could not be updated.")
      }.fallbackTo(errorInternal)
    }
  }

  /**
   * Delete an exist store by id
   *
   * @param id
   * @return number 1 if success
   */
  @ApiOperation(
    value = "Delete store",
    notes = "delete exist store by its id",
    httpMethod = "DELETE"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Success"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def deleteStoreById(id: Long) = SecuredApiAction { implicit request =>
    storeService.deleteStore(id).flatMap {
      case 1 => ok("Store has been successful deleted : " + 1)
      case _ => errorBadRequest("Store could not be deleted.")
    }.fallbackTo(errorInternal)
  }

  // own define
  /**
   * Get all exist store
   *
   * @return json of stores
   */
  @ApiOperation(
    value = "List all stores",
    notes = "Returns all exist stores",
    response = classOf[models.Store],
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error.")
  ))
  def listStores() = SecuredApiAction { implicit request =>
    storeService.listStores().flatMap { // flatMap case data from future reactive
      case stores => ok(stores) // in ok functions return json
      case null => errorNotFound
    }.fallbackTo(errorInternal)
  }

  /**
   * Get stores with pagination
   * @param page
   * @param limit
   * @return json of stores
   */
  @ApiOperation(
    value = "Pagination stores",
    notes = "Returns all exist stores with pagination",
    response = classOf[models.Store],
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error.")
  ))
  def paginationStores(page: Int, limit: Int) = SecuredApiAction { implicit request =>
    storeService.paginationStore(page, limit).flatMap { // flatMap case data from future reactive
      case stores => ok(stores) // in ok functions return json
      case null => errorNotFound
    }.fallbackTo(errorInternal)
  }

  /**
   * Get stores with sorting
   * @param mode
   * @return json of stores
   */
  @ApiOperation(
    value = "Sort stores",
    notes = "Returns all exist stores with sorting",
    response = classOf[models.Store],
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error.")
  ))
  def sortStores(mode: String) = SecuredApiAction { implicit request =>
    storeService.sortStores(mode).flatMap { // flatMap case data from future reactive
      case stores => ok(stores) // in ok functions return json
      case null => errorNotFound
    }.fallbackTo(errorInternal)
  }

}
