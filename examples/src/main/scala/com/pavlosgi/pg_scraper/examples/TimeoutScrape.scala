package com.pavlosgi.pg_scraper.examples

import java.io.File

import com.pavlosgi.pg_scraper.core.dsl._

import scala.concurrent.Await
import scala.concurrent.duration._

object TimeoutScrape extends App {

  val scrapeObj = scrape("http://www.timeout.com/") {
    extract("title", ".post-header h1") ~
      extract(new File(getClass.getResource("/timeout-actions-config.xml").getFile)) ~
      click(".sd2-alternate-hidden-content dd div span a") ~
      extract("info", "#pageContainer #mainContent h2.strapline") ~
      write("restaurants", "#searchText") ~
      click(".submit.searchButton") ~
      extract("first-restaurant", ".tiles > div:first-child h3 a")
  }

  val res = Await.result(scrapeObj.results, 30 seconds)

  for ((k, v) <- res.successMap)
    println(s"$k -> $v")

}
