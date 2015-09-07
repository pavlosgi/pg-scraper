package com.pavlosgi.pg_scraper.core.actions

import scala.collection.immutable.LinearSeq

trait Action{

  val description:String
  val actions:LinearSeq[Action]

  def ~(action: Action): ActionSeq = {
    new ActionSeq(this, action)
  }

  def ++(action:Action) = {
    new ActionSeq(actions ++ action.actions :_*)
  }

  private def recFlatten(actions:LinearSeq[Action]):LinearSeq[Action] = {
    actions.flatMap{
      case seq:ActionSeq => recFlatten(seq.actions)
      case action => action.actions
    }
  }

  def flatten = {
    recFlatten(actions)
  }

  override def toString = description

}
