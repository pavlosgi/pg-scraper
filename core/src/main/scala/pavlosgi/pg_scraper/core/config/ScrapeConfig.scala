package pavlosgi.pg_scraper.core.config

import pavlosgi.pg_scraper.core.actions.Action


case class ScrapeConfig(url:String,action:Action) extends Config
