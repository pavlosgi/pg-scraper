package com.pavlosgi.pg_scraper.core

import com.pavlosgi.pg_scraper.core.actions.{Action, ActionSeq}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

import scala.collection.immutable.LinearSeq

class BaseSpec extends Specification with Mockito{

  def unfold(actionSeq:LinearSeq[Action]):LinearSeq[Action] = {
    actionSeq.flatMap {
      case seq: ActionSeq => unfold(seq.actions)
      case a => a.actions
    }
  }
}
