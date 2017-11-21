package $package$.models

import play.api.libs.json.Json

case class $modelname$(
  parameter1: String,
  parameter2: Option[String],
  telephoneNumber: Option[String],
  emailAddress: Option[String])

object $modelname$ {
  implicit val modelFormat = Json.format[$modelname$]
}