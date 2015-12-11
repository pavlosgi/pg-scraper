package pavlosgi.pg_scraper.examples

import java.io.File

import pavlosgi.pg_scraper.core.dsl._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object TimeoutScrapeFromFile extends App {

  val scrapeObj = scrape(new File(getClass.getResource("/timeout-scrape-config.xml").getFile))

  scrapeObj.results.onFailure {
    case ex => println(ex)
  }

  val res = Await.result(scrapeObj.results, 30 seconds)

  for ((k, v) <- res.successMap)
    println(s"$k -> $v")

}
