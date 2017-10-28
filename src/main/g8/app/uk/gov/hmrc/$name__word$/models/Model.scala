package uk.gov.hmrc.$name;format="word"$.models

import play.api.libs.json.Json

case class Model(parameter1: String,
                 parameter2: Option[String],
                 telephoneNumber: Option[String],
                 emailAddress: Option[String])

object Model {
  implicit val modelFormat = Json.format[Model]
}