package $package$.controllers

import $package$.models.{ $modelnamecamel$ }
import uk.gov.hmrc.play.test.UnitSpec

class $formname$Spec extends UnitSpec {

  "$formname$" should {

    "bind some input fields and return $modelnamecamel$ and fill it back" in {
      val form = $servicenamecamel$Controller.$formname$

      val value = $modelnamecamel$(
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

    "bind all input fields and return $modelnamecamel$ and fill it back" in {
      val form = $servicenamecamel$Controller.$formname$

      val value = $modelnamecamel$(
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
