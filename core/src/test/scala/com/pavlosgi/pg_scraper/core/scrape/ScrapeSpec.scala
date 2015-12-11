package pavlosgi.pg_scraper.core.scrape

import pavlosgi.pg_scraper.core.BaseSpec
import pavlosgi.pg_scraper.core.dsl._

class ScrapeSpec extends BaseSpec {

  "Scrape" should {

    "combine actions correctly" in {

      val scrapeObj = scrape("url"){
        extract("title","selector")
      }

      val combineObj = scrapeObj ~ {
        extract("title2","selector2")
      }

      combineObj.actions.length === 2
    }

    "apply actions correctly" in {

      val scrapeObj = scrape("url"){
        extract("title","selector")
      }

      scrapeObj.actions.length === 1

      val combineObj = scrapeObj {
        extract("title2","selector2")
      }

      combineObj.actions.length === 2
    }

  }

}