package services.impl

import api.Api.Sorting._
import api.Page
import com.google.inject.Inject
import models.{ Category, UserModel }
import models.repositories.IUserRepo
import services.IUserService
import scala.concurrent.duration._

import scala.concurrent.{ Await, Future }

class UserService @Inject() (userRepo: IUserRepo) extends IUserService {

  override def insert(user: UserModel): Future[Int] = userRepo.insert(user)

  override def count(): Future[Int] = userRepo.count

  override def update(id: Long, user: UserModel): Future[Int] = {
    var user1 = UserModel(Option(id), user.user_email, user.user_first_name, user.user_last_name, user.user_password, user.user_confirm_password, user.user_token, user.user_active, user.user_role, user.create_time)
    userRepo.update(user1)
  }

  override def findById(id: Long): Future[UserModel] = userRepo.findById(id)

  override def delete(id: Long): Future[Int] = userRepo.delete(id)

  override def list(sortingFields: Seq[(String, Boolean)], p: Int, s: Int): Future[Page[UserModel]] = Future.successful {
    pages(p, s)(sortingFields.map(sortingFunc): _*)
  }

  def pages(p: Int, s: Int)(sortFuncs: ((UserModel, UserModel) => Boolean)*): Page[UserModel] = {
    val sorted = sortFuncs.foldRight(Await.result(userRepo.list, 10 seconds).toList)((f, items) => items.sortWith(f))
    Page(
      items = sorted.slice((p - 1) * s, ((p - 1) * s) + s),
      page = p,
      size = s,
      total = sorted.size
    )
  }

  def sortingFunc(fieldsWithOrder: (String, Boolean)): (UserModel, UserModel) => Boolean = fieldsWithOrder match {
    case ("user_id", ASC) => _.user_id.get < _.user_id.get
    case ("user_id", DESC) => _.user_id.get > _.user_id.get
    case ("user_first_name", ASC) => (a, b) => (a.user_first_name compareTo b.user_first_name) < 0
    case ("user_first_name", DESC) => (a, b) => (a.user_first_name compareTo b.user_first_name) > 0
    case ("user_last_name", ASC) => (a, b) => (a.user_last_name compareTo b.user_last_name) < 0
    case ("user_last_name", DESC) => (a, b) => (a.user_last_name compareTo b.user_last_name) > 0
    case ("user_role", ASC) => _.user_role < _.user_role
    case ("user_role", DESC) => _.user_role > _.user_role
    case _ => (_, _) => false
  }

  override def findByEmail(email: String): Future[Option[UserModel]] = userRepo.findByEmail(email)

  override def updateActive(token: String, user: UserModel): Future[Int] = {
    var user1 = UserModel(user.user_id, user.user_email, user.user_first_name, user.user_last_name, user.user_password, user.user_confirm_password, user.user_token, true, user.user_role, user.create_time)
    userRepo.updateActive(user1)
  }

  override def findByToken(token: String): Future[Option[UserModel]] = userRepo.findByToken(token)
}
