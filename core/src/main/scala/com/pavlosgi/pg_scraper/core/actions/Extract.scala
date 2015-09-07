package com.pavlosgi.pg_scraper.core.actions

import scala.collection.immutable.LinearSeq


class Extract(val key:String,val selector:String) extends Action {

  val actions = LinearSeq(this)
  val description = s"Extrect ($key -> $selector)"

}
