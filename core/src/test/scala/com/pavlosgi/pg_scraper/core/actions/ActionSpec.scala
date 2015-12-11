package pavlosgi.pg_scraper.core.actions

import pavlosgi.pg_scraper.core.BaseSpec

class ActionSpec extends BaseSpec{

  "ActionSpec" should{
    "compose actions correctly" in{
      val ac1 = new Extract("key","selector")
      val ac2 = new Click("selector")
      val ac3 = ac1 ~ ac2

      ac3.isInstanceOf[ActionSeq] === true
      ac3.actions.length === 2
    }

    "add actions correctly" in{
      val ac1 = new Extract("key","selector")
      val ac2 = new Click("selector")
      val ac3 = ac1 ++ ac2

      ac3.isInstanceOf[ActionSeq] === true
      ac3.actions.length === 2
    }
  }

}
