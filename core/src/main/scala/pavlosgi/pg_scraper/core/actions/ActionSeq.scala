package pavlosgi.pg_scraper.core.actions

import scala.collection.immutable.LinearSeq

class ActionSeq(actionsSeq:Action*) extends Action{

  val actions = LinearSeq(actionsSeq:_*)
  val description = "ActionSequence " + actions.length

}