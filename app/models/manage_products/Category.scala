package models.manage_products
import play.api.libs.json._

// JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax
import slick.driver.PostgresDriver.api._
/**
 * Created by Oudam on 10/12/2016.
 */
case class Category(prod_cate_id: Option[Int], prod_cate_name: String, prod_cate_description: String)

object Category {
  val categoryReads: Reads[Category] = (
    (__ \ "prod_cate_id").readNullable[Int] and
    (__ \ "prod_cate_name").read[String] and
    (__ \ "prod_cate_description").read[String]
  )(Category.apply _)

  val categoryWrites: Writes[Category] = (
    (__ \ "prod_cate_id").writeNullable[Int] and
    (__ \ "prod_cate_name").write[String] and
    (__ \ "prod_cate_description").write[String]
  )(unlift(Category.unapply))

  implicit val categoryFormat: Format[Category] =
    Format(categoryReads, categoryWrites)

}

class TCategory(tag: Tag) extends Table[Category](tag, "product_category_tbl") {

  def id = column[Int]("prod_cate_id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("prod_cate_name")
  def description = column[String]("prod_cate_description")
  def * = (id.?, name, description) <> ((Category.apply _).tupled, Category.unapply)

}