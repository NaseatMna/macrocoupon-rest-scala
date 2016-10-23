package models.manage_products

import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import slick.driver.PostgresDriver.api._
import com.github.tototoshi.slick.PostgresJodaSupport._

/**
 * Created by Oudam on 10/12/2016.
 */
case class Product(prod_id: Option[Long], user_id: Int, prod_cate_id: Int, prod_seo_id: Int, prod_name: String, prod_description: String,
  prod_price: Double, isPublished: Int, createdDate: Option[DateTime], prod_image: String)
object Product {
  val productReads: Reads[Product] = (
    (__ \ "prod_id").readNullable[Long] and
    (__ \ "user_id").read[Int] and
    (__ \ "prod_cate_id").read[Int] and
    (__ \ "prod_seo_id").read[Int] and
    (__ \ "prod_name").read[String] and
    (__ \ "prod_description").read[String] and
    (__ \ "prod_price").read[Double] and
    (__ \ "isPublished").read[Int] and
    (__ \ "createdDate").readNullable[DateTime] and
    (__ \ "prod_image").read[String]
  )(Product.apply _)

  val productWrites: Writes[Product] = (
    (__ \ "prod_id").writeNullable[Long] and
    (__ \ "user_id").write[Int] and
    (__ \ "prod_cate_id").write[Int] and
    (__ \ "prod_seo_id").write[Int] and
    (__ \ "prod_name").write[String] and
    (__ \ "prod_description").write[String] and
    (__ \ "prod_price").write[Double] and
    (__ \ "isPublished").write[Int] and
    (__ \ "createdDate").writeNullable[DateTime] and
    (__ \ "prod_image").write[String]
  )(unlift(Product.unapply))

  implicit val productFormat: Format[Product] =
    Format(productReads, productWrites)
}

class TProduct(tag: Tag) extends Table[Product](tag, "product_tbl") {
  val categories = TableQuery[TCategory]
  val users = TableQuery[TUser]
  //implicit val dateColumnType = MappedColumnType.base[DateTime, Long](d => d.getMillis, d => new DateTime(d))

  def id = column[Long]("prod_id", O.PrimaryKey, O.AutoInc)
  def user_id = column[Int]("user_id")
  def cate_id = column[Int]("prod_cate_id")
  def seo_id = column[Int]("prod_seo_id")
  def name = column[String]("prod_name")
  def description = column[String]("prod_description")
  def price = column[Double]("prod_price")
  def isPublished = column[Int]("isPublished")
  def createdDate = column[DateTime]("createdDate")
  def image = column[String]("prod_image")

  def * = (id.?, user_id, cate_id, seo_id, name, description, price, isPublished, createdDate.?, image) <> ((Product.apply _).tupled, Product.unapply)

  def categoryFK = foreignKey("product_category_fk", cate_id, categories)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)
  def userFK = foreignKey("product_user_fk", cate_id, users)(_.id, onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

}
