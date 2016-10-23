package services

import api.Page
import com.google.inject.ImplementedBy
import models.UserModel
import services.impl.UserService

import scala.concurrent.Future

@ImplementedBy(classOf[UserService])
trait IUserService {

  def insert(user: UserModel): Future[Int]
  def update(id: Long, user: UserModel): Future[Int]
  def updateActive(token: String, user: UserModel): Future[Int]
  def delete(id: Long): Future[Int]
  def list(sortingFields: Seq[(String, Boolean)], p: Int, s: Int): Future[Page[UserModel]]
  def findById(id: Long): Future[UserModel]
  def findByEmail(email: String): Future[Option[UserModel]]
  def count(): Future[Int]
  def findByToken(token: String): Future[Option[UserModel]]

}
