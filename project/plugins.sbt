resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.7.0-SNAPSHOT")

addSbtPlugin("com.typesafe.akka" % "akka-sbt-plugin" % "2.2.3")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.1")

addSbtPlugin("org.scoverage" %% "sbt-scoverage" % "0.99.7.1")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "0.99.7.1")

addSbtPlugin("org.scoverage" %% "sbt-coveralls" % "0.99.0")

resolvers += "bigtoast-github" at "http://bigtoast.github.com/repo/"

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.4")
