package controllers

import java.util.UUID

import api.ApiError._
import api.JsonCombinators._
import io.swagger.annotations._
import models.{ UserModel, User, ApiToken }
import org.joda.time.{ Period, DateTime }
import play.api.Environment
import play.api.libs.mailer.{ Email, MailerClient }
import play.api.libs.json._
import play.api.libs.functional.syntax._
import akka.actor.ActorSystem
import play.api.mvc.Action
import services.IUserService
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.Inject
import play.api.i18n.{ Messages, MessagesApi }

@Api(value = "/User As Merchants")
class MerchantsController @Inject() (val messagesApi: MessagesApi, system: ActorSystem, userService: IUserService, mailer: MailerClient, environment: Environment) extends api.ApiController {
  val sortingFields = Seq("user_id", "user_first_name", "user_last_name", "user_role")

  //insert a user into databse
  @ApiOperation(
    value = "Add new user",
    notes = "Post Method",
    response = classOf[models.UserModel],
    httpMethod = "POST"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Data for add user", required = true, dataType = "String", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 201, message = "Success"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def insert() = SecuredApiActionWithBody { implicit request =>
    readFromRequest[UserModel] {
      case (user) =>
        userService.findByEmail(user.user_email).flatMap {
          case Some(anotherUser) => errorCustom("api.error.signup.email.exists")
          case None => userService.insert(user).flatMap {
            case 1 => created(Json.obj {
              "message" -> "User have been inserted!"
            })
            case _ => errorBadRequest("User could not be inserted.")
          }
        }

    }
  }

  /**
   * Update an exist user by id
   *
   * @param id
   * @return number 1 if success
   */
  @ApiOperation(
    value = "Update user",
    notes = "update existing user by its id",
    httpMethod = "PUT"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Data for update user", required = true, dataType = "String", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Success"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def update(id: Long) = SecuredApiActionWithBody { implicit request =>
    readFromRequest[UserModel] { user =>
      userService.update(id, user).flatMap {
        case 1 => ok(Json.obj { "message" -> "User have been updated!" })
        case _ => errorBadRequest("User could not be updated.")
      }.fallbackTo(errorInternal)
    }
  }
  /**
   * Get an user by id
   *
   * @param id
   * @return json of user
   */
  @ApiOperation(value = "Get user by id", notes = "Returns exist user by id", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error.")
  ))
  def listUserById(id: Long) = SecuredApiAction { implicit request =>
    userService.findById(id).flatMap {
      case user => ok(Json.toJson(user))
      case null => errorNotFound
    }.fallbackTo(errorInternal)
  }

  /**
   * List all users
   *
   * @return json list of users
   */
  @ApiOperation(value = "Get all users", notes = "Returns list of all users", httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Ok"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error.")
  ))
  def listAllUser(sort: Option[String], page: Int, size: Int) = SecuredApiAction { implicit request =>
    sortedPage(sort, sortingFields, default = "user_id") { sortingFields =>
      userService.list(sortingFields, page, size)
    }.fallbackTo(errorInternal)
  }

  /**
   * Delete an user by id
   *
   * @param id
   * @return number 1 if success
   */
  @ApiOperation(
    value = "Delete user",
    notes = "delete exist user by user's id",
    httpMethod = "DELETE"
  )
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "Success"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def deleteUserById(id: Long) = SecuredApiAction { implicit request =>
    userService.delete(id).flatMap {
      case 1 => ok(Json.obj { "message" -> "delete already!" })
      case _ => errorBadRequest("can not be deleted.")
    }.fallbackTo(errorInternal)
  }

  /*
   send mail
   */
  @ApiOperation(
    value = "send mail",
    notes = "send mail",
    httpMethod = "GET"
  )
  def send = Action {
    val cid = "1234"
    val email = Email(
      "Simple email",
      "Mister FROM <caoruth1013@gmail.com>",
      Seq("Miss TO <kanel@gmail.com>"),
      bodyText = Some("A text message"),
      bodyHtml = Some(s"""<html><body><p>An <b>html</b> message with cid <img src="cid:$cid"></p></body></html>""")
    )
    val id = mailer.send(email)
    Ok(s"Email is sent!")
  }

  implicit val loginIfoReads: Reads[Tuple2[String, String]] = (
    (__ \ "user_email").read[String](Reads.email) and
      (__ \ "user_password").read[String] tupled
  )

  //login
  @ApiOperation(
    value = "user login",
    notes = "Post Method",
    response = classOf[models.UserModel],
    httpMethod = "POST"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "please input user's email and passowrd", required = true, dataType = "String", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 201, message = "Success"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def login = ApiActionWithBody { implicit request =>
    readFromRequest[Tuple2[String, String]] {
      case (email, pwd) =>
        userService.findByEmail(email).flatMap {
          case None => errorUserNotFound
          case Some(user) => {
            if (user.user_password != pwd) errorUserNotFound
            else if (!user.user_active) errorUserInactive
            else ApiToken.create(request.apiKeyOpt.get, user.user_id.get).flatMap { token =>
              ok(Json.obj(
                "message" -> "You login successfully!",
                "token" -> token,
                "minutes" -> 10
              ))
            }
          }
        }
    }
  }

  @ApiOperation(
    value = "user logout",
    notes = "user logout",
    httpMethod = "POST"
  )
  def logout = SecuredApiAction { implicit request =>
    ApiToken.delete(request.token).flatMap { _ =>
      ok(Json.obj { "message" -> "You are now logged out!" })
    }

  }

  implicit val registInfoReads: Reads[Tuple5[String, String, String, String, String]] = (

    (__ \ "user_email").read[String](Reads.email) and
      (__ \ "user_first_name").read[String] and
      (__ \ "user_last_name").read[String] and
      (__ \ "user_password").read[String](Reads.minLength[String](6)) and
      (__ \ "user_confirm_password").read[String](Reads.minLength[String](6)) tupled
  )

  //login
  @ApiOperation(
    value = "user register",
    notes = "Post Method",
    response = classOf[models.UserModel],
    httpMethod = "POST"
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "please give the user's email, first name,last name, password,confirmed password", required = true, dataType = "String", paramType = "body")
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 201, message = "Success"),
    new ApiResponse(code = 400, message = "Bad Request"),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def regist = ApiActionWithBody { implicit request =>
    readFromRequest[Tuple5[String, String, String, String, String]] {
      case (user_email, user_first_name, user_last_name, user_password, user_confirm_password) =>
        userService.findByEmail(user_email).flatMap {
          case Some(anotherUser) => errorCustom("api.error.signup.email.exists")
          case None =>
            val token = UUID.randomUUID().toString + user_email
            userService.insert(UserModel(Some(0), user_email, user_first_name, user_last_name, user_password, user_confirm_password,
              token, false, 3, new DateTime())).flatMap {
              case _ =>
                val url = "http://localhost:9000/merchants/activeuser/" + token
                val email = Email(
                  subject = Messages("Please Active your user account"),
                  from = Messages("caoruth1013@gmail.com"),
                  to = Seq(user_email),
                  bodyText = Some("activate user account"),
                  bodyHtml = Some("<html><body><p><a href=" + url + ">Click here to active your account</a></p></body></html>")
                )
                mailer.send(email)
                ok(Json.obj { "message" -> "mail has been already sent!" })

            }
        }
    }

  }

  @ApiOperation(
    value = "activate the user",
    notes = "activate the user",
    httpMethod = "GET"
  )
  def activate(token: String) = ApiAction { implicit request =>
    userService.findByToken(token).flatMap {
      case None => ok(Json.obj { "message" -> "Your token is not correct!" })
      case Some(user) => {
        val duration = new Period(user.create_time, new DateTime()).getMinutes
        if (duration > 2 && !user.user_active) {
          userService.delete(user.user_id.get)
          ok(Json.obj { "message" -> "your token has already expired!" })
        } else if (user.user_active) {
          ok(Json.obj { "message" -> "no need click again!" })
        } else {
          userService.updateActive(token, user)
          ok(Json.obj { "message" -> "Your account has already active!" })
        }
      }
    }
  }

}
