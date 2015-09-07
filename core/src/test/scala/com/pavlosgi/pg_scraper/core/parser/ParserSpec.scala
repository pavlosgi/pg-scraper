package com.pavlosgi.pg_scraper.core.parser

import java.io.File

import com.pavlosgi.pg_scraper.core.BaseSpec

import scala.util.{Failure, Success}


object ParserSpec extends BaseSpec {

  "ParserSpec" should {

    "parse correctly for scrape config xml" in {
      val config = Parser.parseScrapeConfigFile(new File(getClass.getResource("/valid-scrape-config.xml").getFile))
      config.isInstanceOf[Success[Any]] === true
      val actions = config.get.action.actions
      unfold(actions).length === 4
    }

    "parse correctly for scrape config json" in {
      val config = Parser.parseScrapeConfigFile(new File(getClass.getResource("/valid-scrape-config.json").getFile))
      config.isInstanceOf[Success[Any]] === true
      val actions = config.get.action.actions
      unfold(actions).length === 4
    }

    "parse correctly for actions config xml" in {
      val config = Parser.parseActionSequenceFile(new File(getClass.getResource("/valid-actions-config.xml").getFile))
      config.isInstanceOf[Success[Any]] === true
      val actions = config.get.actions
      unfold(actions).length === 1
    }

    "parse correctly for actions config json" in {
      val config = Parser.parseActionSequenceFile(new File(getClass.getResource("/valid-actions-config.json").getFile))
      config.isInstanceOf[Success[Any]] === true
      val actions = config.get.actions
      unfold(actions).length === 1
    }

    "fail to parse scrape config file with too many steps in xml" in {
      val config = Parser.parseScrapeConfigFile(new File(getClass.getResource("/invalid-scrape-config-many-steps.xml").getFile))
      config.isInstanceOf[Failure[Any]] === true
    }

    "fail to parse scrape config file with no url in xml" in {
      val config = Parser.parseScrapeConfigFile(new File(getClass.getResource("/invalid-scrape-config-no-url.xml").getFile))
      config.isInstanceOf[Failure[Any]] === true
    }

    "fail to parse scrape config file with wrong tag in xml" in {
      val config = Parser.parseScrapeConfigFile(new File(getClass.getResource("/invalid-scrape-config-wrong-tag.xml").getFile))
      config.isInstanceOf[Failure[Any]] === true
    }

    "fail to parse scrape config file with too many steps in json" in {
      val config = Parser.parseScrapeConfigFile(new File(getClass.getResource("/invalid-scrape-config-many-steps.json").getFile))
      config.isInstanceOf[Failure[Any]] === true
    }

    "fail to parse scrape config file with no url in json" in {
      val config = Parser.parseScrapeConfigFile(new File(getClass.getResource("/invalid-scrape-config-no-url.json").getFile))
      config.isInstanceOf[Failure[Any]] === true
    }

    "fail to parse scrape config file with wrong tag in json" in {
      val config = Parser.parseScrapeConfigFile(new File(getClass.getResource("/invalid-scrape-config-wrong-tag.json").getFile))
      config.isInstanceOf[Failure[Any]] === true
    }
  }

}
