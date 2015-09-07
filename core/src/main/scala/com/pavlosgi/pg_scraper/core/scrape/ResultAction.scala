package com.pavlosgi.pg_scraper.core.scrape

import com.pavlosgi.pg_scraper.core.actions.Action

sealed abstract class ResultAction(action:Action)

case class FailedAction(action:Action,error:Throwable) extends ResultAction(action)

case class SuccessAction(action:Action,result:Map[String,Any] = Map()) extends ResultAction(action)