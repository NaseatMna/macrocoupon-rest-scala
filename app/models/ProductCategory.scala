package models
import play.api.libs.json._

// JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax
import slick.driver.PostgresDriver.api._

/**
 * Created by Oudam on 9/20/2016.
 */

/**
 * case class Category a new type
 * use for create type of product_category_table
 *
 * @param category_id
 * @param category_name
 * @param category_description
 * @param category_image
 */
case class Category(category_id: Option[Long], category_name: String, category_description: String, category_image: String)

object Category {
  val categoryReads: Reads[Category] = (
    (__ \ "category_id").readNullable[Long] and
    (__ \ "category_name").read[String] and
    (__ \ "category_description").read[String] and
    (__ \ "category_image").read[String]
  )(Category.apply _)

  val categoryWrites: Writes[Category] = (
    (__ \ "category_id").writeNullable[Long] and
    (__ \ "category_name").write[String] and
    (__ \ "category_description").write[String] and
    (__ \ "category_image").write[String]
  )(unlift(Category.unapply))

  implicit val categoryFormat: Format[Category] =
    Format(categoryReads, categoryWrites)

}

class ProductCategory(tag: Tag) extends Table[Category](tag, "mccp_product_category_tbl") {

  def id = column[Long]("category_id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("category_name")
  def description = column[String]("category_description")
  def image = column[String]("category_image")

  def * = (id.?, name, description, image) <> ((Category.apply _).tupled, Category.unapply)
}

