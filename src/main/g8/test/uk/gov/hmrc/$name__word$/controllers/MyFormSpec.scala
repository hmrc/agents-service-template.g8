package uk.gov.hmrc.$name;format="word"$.controllers

import uk.gov.hmrc.$name;format="word"$.models.{Model}
import uk.gov.hmrc.play.test.UnitSpec

class MyFormSpec extends UnitSpec {

  "MyForm" should {

    "bind some input fields and return Model and fill it back" in {
      val form = $name;format="Camel"$Controller.MyForm

      val value = Model(
        parameter1 = "SomeValue",
        parameter2 = None,
        telephoneNumber = None,
        emailAddress = None
      )

      val fieldValues = Map(
        "parameter1" -> "SomeValue"
      )

      form.bind(fieldValues).value shouldBe Some(value)
      form.fill(value).data shouldBe fieldValues
    }

    "bind all input fields and return Model and fill it back" in {
      val form = $name;format="Camel"$Controller.MyForm

      val value = Model(
        parameter1 = "SomeValue",
        parameter2 = Some("AA1 1AA"),
        telephoneNumber = Some("098765321"),
        emailAddress = Some("foo@bar.com")
      )

      val fieldValues = Map(
        "parameter1" -> "SomeValue",
        "parameter2" -> "AA1 1AA",
        "telephoneNumber" -> "098765321",
        "emailAddress" -> "foo@bar.com"
      )

      form.bind(fieldValues).value shouldBe Some(value)
      form.fill(value).data shouldBe fieldValues
    }
  }
}
