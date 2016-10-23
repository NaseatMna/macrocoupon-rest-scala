package service_test.controller

import play.api.mvc._
/**
 * Created by Naseat on 10/20/2016.
 */
class exampleController extends Controller {

  def index() = Action {
    Ok("Hello OK")
  }

}
