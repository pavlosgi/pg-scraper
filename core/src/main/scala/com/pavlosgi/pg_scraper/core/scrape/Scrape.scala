package com.pavlosgi.pg_scraper.core.scrape

import java.io.File
import java.util.logging.Level

import com.pavlosgi.pg_scraper.core.actions.{Write, Action, Click, Extract}
import com.pavlosgi.pg_scraper.core.parser.Parser
import com.typesafe.scalalogging.slf4j.Logger
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.{By, WebDriver}
import org.slf4j.LoggerFactory

import scala.collection.immutable.LinearSeq
import scala.concurrent.Future
import scala.util.{Failure, Success}


class Scrape private(val url: String, val actions: LinearSeq[Action]) {

  val logger = Logger(LoggerFactory.getLogger(getClass.getName))

  lazy val results: Future[Results] = {

    import scala.concurrent.ExecutionContext.Implicits.global
    logger.info("Scraping started for {}",url)
    Future {
      val driver = new HtmlUnitDriver
      driver.get(url)

      val results = actions.map { action =>
        execute(action, driver)
      }
      logger.info("Scraping finished for {}",url)
      driver.quit
      new Results(results)
    }
  }

  def rescrape = {
    new Scrape(url,actions).results
  }

  def ~(action: Action) = {
    new Scrape(this.url, this.actions ++ action.flatten)
  }

  def apply(action: Action) = {
    new Scrape(this.url, this.actions ++ action.flatten)
  }

  def execute(action: Action, driver: WebDriver): ResultAction = {
    try {
      action match {
        case extract: Extract => {
          val value = driver.findElement(By.cssSelector(extract.selector)).getText
          SuccessAction(action, Map(extract.key -> value))
        }
        case click: Click => {
          driver.findElement(By.cssSelector(click.selector)).click
          SuccessAction(action)
        }
        case write: Write => {
          driver.findElement(By.cssSelector(write.selector)).sendKeys(write.input)
          SuccessAction(action)
        }
      }
    }
    catch {
      case ex: Throwable => {
        logger.debug("Failed to complete action {} with {}",action,ex)
        FailedAction(action, ex)
      }
    }
  }
}

object Scrape {

  java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.SEVERE)
  System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog")

  def apply(file: File) = {
    Parser.parseScrapeConfigFile(file) match {
      case Success(conf) => new Scrape(conf.url, conf.action.flatten)
      case Failure(ex) => throw ex
    }
  }

  def apply(url: String, action: Action) = {
    new Scrape(url, action.flatten)
  }
}
