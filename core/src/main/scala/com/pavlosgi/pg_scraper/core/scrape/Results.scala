package com.pavlosgi.pg_scraper.core.scrape

import scala.collection.immutable.LinearSeq

class Results(results: LinearSeq[ResultAction]) {

  private val (f, s) = results.partition(_.isInstanceOf[FailedAction])

  val failures = f.map(_.asInstanceOf[FailedAction])
  val successes = s.map(_.asInstanceOf[SuccessAction])

  val successMap = {
    val seq = successes.map(_.result)
    if(!seq.isEmpty) seq.reduce(_ ++ _)
    else Map[String,String]()
  }

}
