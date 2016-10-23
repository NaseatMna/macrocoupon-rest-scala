package controllers

import javax.inject.Inject

import api.ApiController
import io.swagger.annotations._
import play.api.i18n.{ Messages, MessagesApi }
import play.api.mvc.{ Action, Controller }

@Api(value = "/SampleTodo", description = "Operation like GET,POST and Upload Image")
class SampleSwaggerController @Inject() (val messagesApi: MessagesApi) extends ApiController {

  @ApiOperation(
    value = "Get a content type by its name",
    notes = "Returns a content type",
    httpMethod = "GET"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Success"),
    new ApiResponse(code = 400, message = "Bad request. Called when a route is found, but it was not possible to bind the request parameters"),
    new ApiResponse(code = 403, message = "Forbidden"),
    new ApiResponse(code = 500, message = "Internal server error")
  // more status code you want to put.
  ))
  def getTodos = Action { implicit request =>
    Ok(Messages("example.test.translate"))
  }

  @ApiOperation(
    value = "Get a content type by its name",
    notes = "Returns a content type",
    response = classOf[models.User], // Here you add to add your model case class like this model.yourmodelcaseclasse
    httpMethod = "POST"
  )
  @ApiResponses(Array( // new ApiResponse(code = 404, message = "Content Type not found")
  ))
  def postTodos = Action { implicit request =>
    Ok(Messages("example.test.translate"))
  }
}
