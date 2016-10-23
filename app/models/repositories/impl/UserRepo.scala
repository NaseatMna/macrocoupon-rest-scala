package models.repositories.impl

import com.google.inject.Inject
import models.{ UserTable, UserModel }
import models.repositories.IUserRepo
import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._

import scala.concurrent.Future
import scala.util.control.Exception

class UserRepo @Inject() (dbConfigProvider: DatabaseConfigProvider) extends IUserRepo {

  val dbConfig = dbConfigProvider.get[JdbcProfile]
  val users = TableQuery[UserTable]

  /**
   * Filter user with id
   */
  private def filterQuery(id: Long): Query[UserTable, UserModel, Seq] = {
    try users.filter(_.id === id)
  }

  /*
   Filter user with email
  */
  private def filterQueryByEmail(email: String): Query[UserTable, UserModel, Seq] = {
    users.filter(_.email === email)
  }

  /*
  Filter user with token
   */
  private def filterByToken(token: String): Query[UserTable, UserModel, Seq] = {
    users.filter(_.token === token)
  }

  private def count(filter: String): Future[Int] = {
    dbConfig.db.run(users.filter(_.email.toLowerCase like filter.toLowerCase()).length.result)

  }

  override def insert(user: UserModel): Future[Int] = {
    dbConfig.db.run(users += user)

  }

  override def count(): Future[Int] = {
    dbConfig.db.run(users.length.result)

  }

  override def update(user: UserModel): Future[Int] = {
    dbConfig.db.run(filterQuery(user.user_id.get).update(user))
  }

  override def findById(id: Long): Future[UserModel] = {
    dbConfig.db.run(filterQuery(id).result.head)

  }

  override def delete(id: Long): Future[Int] = {
    dbConfig.db.run(filterQuery(id).delete)
  }

  override def findByEmail(email: String): Future[Option[UserModel]] = {
    dbConfig.db.run(filterQueryByEmail(email).result.headOption)
  }

  override def list(): Future[Seq[UserModel]] = {
    val query =
      for {
        user <- users if user != null
      } yield (user)
    dbConfig.db.run(query.result)
  }

  override def updateActive(user: UserModel): Future[Int] = {
    dbConfig.db.run(filterByToken(user.user_token).update(user))
  }

  override def findByToken(token: String): Future[Option[UserModel]] = {
    dbConfig.db.run(filterByToken(token).result.headOption)
  }
}