package $package$.models

import play.api.libs.json.Json

case class $modelnamecamel$(
  parameter1:      String,
  parameter2:      Option[String],
  telephoneNumber: Option[String],
  emailAddress:    Option[String]
)

object $modelnamecamel$ {
  implicit val modelFormat = Json.format[$modelnamecamel$]
}