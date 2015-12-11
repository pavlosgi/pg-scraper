package pavlosgi.pg_scraper.core.actions

import scala.collection.immutable.LinearSeq

class NoAction extends Action{
  val actions = LinearSeq()
  val description = s"No Action"
}