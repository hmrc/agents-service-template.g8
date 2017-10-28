package uk.gov.hmrc.$name;format="word"$.stubs

import com.github.tomakehurst.wiremock.common.LocalNotifier.notifier
import com.github.tomakehurst.wiremock.matching.{MatchResult, StringValuePattern}
import com.jayway.jsonpath.internal.JsonContext

class MatchesJsonPathValue(path: String, value: String) extends StringValuePattern(path) {

    def getMatchesJsonPath: String = expectedValue

    override def `match`(json: String): MatchResult =  {
        MatchResult.of(isJsonPathMatch(json))
    }

    def isJsonPathMatch(json: String): Boolean =  {
        try {
          (new JsonContext().parse(json).read(expectedValue)) == value
        } catch {
          case e: Exception =>
            val error = if (e.getMessage.equalsIgnoreCase("invalid path")) {
                "the JSON path didn't match the document structure"
            } else if (e.getMessage.equalsIgnoreCase("invalid container object")) {
                "the JSON document couldn't be parsed"
            } else {
                "of error '" + e.getMessage + "'"
            }
            val message = String.format(
                "Warning: JSON path expression '%s' failed to match document '%s' because %s",
                expectedValue, value, error)
            notifier().info(message)
            false
        }
    }
}
