package com.pavlosgi.pg_scraper.core.actions

import scala.collection.immutable.LinearSeq


class Click(val selector:String) extends Action{
  val actions = LinearSeq(this)
  val description = s"Click ($selector)"
}
