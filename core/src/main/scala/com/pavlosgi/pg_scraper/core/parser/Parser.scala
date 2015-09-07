package com.pavlosgi.pg_scraper.core.parser

import java.io.File

import com.pavlosgi.pg_scraper.core.actions.{Write, Click, Extract}
import com.pavlosgi.pg_scraper.core.config.ScrapeConfig
import com.typesafe.scalalogging.slf4j.Logger
import org.json4s.DefaultFormats
import org.json4s.JsonAST.JValue
import org.json4s.native.JsonMethods._
import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.Try
import scala.xml.{Elem, Node, NodeSeq, XML}


object Parser {

  val logger = Logger(LoggerFactory.getLogger(getClass.getName))

  implicit val formats = DefaultFormats

  def parseScrapeConfigFile(file: File) = Try {
    try {
      val content = Source.fromFile(file).mkString

      file.getAbsolutePath match {
        case path if path.endsWith(".xml") => parseScrapeConfig(XML.loadString(content))
        case path if path.endsWith(".json") => parseScrapeConfig(parse(content))
        case _ => throw new InvalidFileException("File format not supported")
      }
    } catch {
      case ex: Throwable => {
        logger.debug("Failed to complete parsing of file {} with {}", file, ex)
        throw ex
      }
    }
  }

  def parseActionSequenceFile(file: File) = Try {
    try {
      val content = Source.fromFile(file).mkString

      file.getAbsolutePath match {
        case path if path.endsWith(".xml") => parseActionSequence(XML.loadString(content))
        case path if path.endsWith(".json") => parseActionSequence(parse(content))
        case _ => throw new InvalidFileException("File format not supported")
      }
    } catch {
      case ex: Throwable => {
        logger.debug("Failed to complete parsing of file {} with {}", file, ex)
        throw ex
      }
    }
  }

  implicit class ExtendedNodeSeq(nodeSeq: NodeSeq) {
    def textOption: Option[String] = {
      val text = nodeSeq.text
      if (text == null || text.length == 0) None else Some(text)
    }
  }

  private def parseActionSequence(elem: Elem) = {

    val extractions = (elem \\ "pg-scrape" \ "action-sequence" \ "extract").map { node: Node =>
      val step = (node \ "@step").text.toInt
      val key = (node \ "key").text
      val selector = (node \ "selector").text
      step -> new Extract(key, selector)
    }

    val clicks = (elem \\ "pg-scrape" \ "action-sequence" \ "click").map { node: Node =>
      val step = (node \ "@step").text.toInt
      val selector = (node \ "selector").text
      step -> new Click(selector)
    }

    val writes = (elem \\ "pg-scrape" \ "action-sequence" \ "write").map { node: Node =>
      val step = (node \ "@step").text.toInt
      val input = (node \ "input").text
      val selector = (node \ "selector").text
      step -> new Write(input,selector)
    }

    val actions = extractions ++ clicks ++ writes

    if (actions.map(_._1).distinct.size != actions.size)
      throw new InvalidFileException("Steps with the same number found")

    actions.sortBy(_._1).map(_._2).reduceLeft(_ ~ _)
  }

  private def parseScrapeConfig(elem: Elem) = {
    val url = (elem \\ "pg-scrape" \ "url").textOption.getOrElse {
      throw new InvalidFileException("Url not found in file")
    }

    val actionSeq = parseActionSequence(elem)
    ScrapeConfig(url, actionSeq)
  }

  private def parseActionSequence(json: JValue) = {
    val listOfActions = (json \ "pg-scrape" \ "action-sequence").children.map { value =>
      val typeOfAction = (value \ "type").extract[String]
      val step = (value \ "step").extract[Int]
      val selector = (value \ "selector").extract[String]

      typeOfAction match {
        case "extract" =>
          val key = (value \ "key").extract[String]
          step -> new Extract(key, selector)
        case "click" =>
          step -> new Click(selector)
        case "write" =>
          val input = (value \ "input").extract[String]
          step -> new Write(input,selector)
        case _ => throw new InvalidFileException("Steps with the same number found")
      }
    }

    if (listOfActions.map(_._1).distinct.size != listOfActions.size)
      throw new InvalidFileException("Steps with the same number found")

    listOfActions.sortBy(value => value._1).map(_._2).reduceLeft(_ ~ _)
  }

  private def parseScrapeConfig(json: JValue) = {
    val url = (json \ "pg-scrape" \ "url").extract[String]
    val actionSeq = parseActionSequence(json)
    ScrapeConfig(url, actionSeq)
  }

}
