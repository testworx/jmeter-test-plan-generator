import geb.spock.GebReportingSpec
import geb.Browser

class GoogleSearchSpec extends GebReportingSpec {

  def "I can search for Wikipedia"() {
      given:
        Browser.drive {
          go "http://google.com/ncr"

          // make sure we actually got to the page
          assert title == "Google"

          // enter wikipedia into the search field
          when:
          $("input", name: "q").value("wikipedia")

          // wait for the change to results page to happen
          // (google updates the page dynamically without a new request)
          waitFor { title.endsWith("Google Search") }

          // is the first link to wikipedia?
          def firstLink = $("div#rso").find(" div.g h3 a", 0)

          // then:
          // assert firstLink.text().toLowerCase().contains("bbc")

          and:
          // click the link
          firstLink.click()

          // then:
          // // wait for Google's javascript to redirect to Wikipedia
          // waitFor {
          //   title.toLowerCase().contains("BBC")
          // }
  }
}
}
