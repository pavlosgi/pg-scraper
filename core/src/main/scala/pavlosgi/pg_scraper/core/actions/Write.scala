package pavlosgi.pg_scraper.core.actions

import scala.collection.immutable.LinearSeq


class Write(val input:String, val selector:String) extends Action{
  val actions = LinearSeq(this)
  val description = s"Write ($selector)"
}
