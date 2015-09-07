package com.pavlosgi.pg_scraper.core.dsl

import com.pavlosgi.pg_scraper.core.BaseSpec

class DslSpec extends BaseSpec {

  "DslSpec" should {

    "create an extract object" in {
      val scrapeObj = scrape("www.something.com") {
        extract("key", "selector")
      }
      scrapeObj.actions.length === 1
    }

    "create a click object" in {
      val scrapeObj = scrape("http://www.something.com/") {
        click("button")
      }
      scrapeObj.actions.length === 1
    }

    "chain any combination of object" in {
      val scrapeObj = scrape("http://www.something.com/") {
        extract("key", "selector") ~
        extract("key2", "selector2") ~
        click("button") ~
        extract("key3", "selector3") ~
        click("button2")
      }
      scrapeObj.actions.length === 5
    }
  }
}