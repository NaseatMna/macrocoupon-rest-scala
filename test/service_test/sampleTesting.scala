
import play.api.libs.ws._
import play.api.test._
import service_test.controller.exampleController
class sampleTesting extends PlaySpecification {
  "Application" should {
    "be reachable" in new WithServer {
      val response = await(WS.url("http://localhost:" + port).get()) //1
      response.status must equalTo(OK) //2
      response.body must contain("Semaphore Community Library") //3
    }
  }

}

