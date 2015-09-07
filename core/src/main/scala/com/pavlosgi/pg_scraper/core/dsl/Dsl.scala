package com.pavlosgi.pg_scraper.core

import java.io.File

import com.pavlosgi.pg_scraper.core.actions._
import com.pavlosgi.pg_scraper.core.parser.Parser
import com.pavlosgi.pg_scraper.core.scrape._

import scala.util.{Failure, Success}


package object dsl {

  def extract(key:String,selector:String):Extract = {
    new Extract(key,selector)
  }

  def extract(file:File) = {
    Parser.parseActionSequenceFile(file) match {
      case Success(conf) => new ActionSeq(conf.actions:_*)
      case Failure(ex) => throw ex
    }
  }

  def write(input:String, selector:String) ={
    new Write(input,selector)
  }

  def click(selector:String) ={
    new Click(selector)
  }

  object scrape{
    def apply(url:String): Scrape = {
      Scrape(url, new NoAction)
    }
    def apply(file:File): Scrape = {
      Scrape(file)
    }
  }
}


