import sbt._
import Keys._
import java.net.{URL, URLClassLoader}

object ScalatestBuild extends Build {
  type Settings = Seq[sbt.Def.Setting[_]]

  lazy val scalatest = Project("scalatest", file(".")).settings(mainSettings: _*)
  lazy val gentests  = Project("gentests", file("gentests")).settings(genSettings: _*).dependsOn(scalatest  % "test->test")

  def coreSettings = Seq(
    organization := "org.scalatest",
    version := "2.0-SNAPSHOT",
    scalaVersion := "2.10.3",
    resolvers += "Sonatype Public" at "https://oss.sonatype.org/content/groups/public",
    libraryDependencies ++= Seq(
       "org.scala-sbt"             %  "test-interface"         % "1.0"             % "optional",
       "org.scalacheck"            %% "scalacheck"             % "1.10.0"          % "optional",
       "org.easymock"              %  "easymockclassextension" % "3.1"             % "optional",
       "org.jmock"                 %  "jmock-legacy"           % "2.5.1"           % "optional",
       "org.mockito"               %  "mockito-all"            % "1.9.0"           % "optional",
       "org.testng"                %  "testng"                 % "6.3.1"           % "optional",
       "com.google.inject"         %  "guice"                  % "3.0"             % "optional",
       "junit"                     %  "junit"                  % "4.10"            % "optional",
       "org.seleniumhq.selenium"   %  "selenium-java"          % "2.35.0"          % "optional",
       "org.apache.ant"            %  "ant"                    % "1.7.1"           % "optional",
       "net.sourceforge.cobertura" %  "cobertura"              % "1.9.1"           % "test",
       "commons-io"                %  "commons-io"             % "1.3.2"           % "test",
       "org.eclipse.jetty"         %  "jetty-server"           % "8.1.8.v20121106" % "test",
       "org.eclipse.jetty"         %  "jetty-webapp"           % "8.1.8.v20121106" % "test",
       "asm"                       %  "asm"                    % "3.3.1"           % "optional",
       "org.pegdown"               %  "pegdown"                % "1.1.0"           % "optional"
    )
  )

  def mainSettings = coreSettings ++ generators ++ Seq[sbt.Def.Setting[_]](
    scalacOptions ++= Seq("-no-specialization", "-feature", "-target:jvm-1.5"),
    initialCommands in console := """|import org.scalatest._
                                    |import org.scalautils._
                                    |import Matchers._""".stripMargin,
    ivyXML :=
     <dependency org="org.eclipse.jetty.orbit" name="javax.servlet" rev="3.0.0.v201112011016">
       <artifact name="javax.servlet" type="orbit" ext="jar"/>
     </dependency>,
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value, // this is needed to compile macro
    sourceGenerators in Compile <+=
       (baseDirectory, sourceManaged in Compile) map genFiles("gengen", "GenGen.scala")(GenGen.genMain(_, scalaVersion.value)),
    sourceGenerators in Compile <+=
       (baseDirectory, sourceManaged in Compile) map genFiles("gentables", "GenTable.scala")(GenTable.genMain(_, scalaVersion.value)),
    sourceGenerators in Compile <+=
       (baseDirectory, sourceManaged in Compile) map genFiles("genmatchers", "MustMatchers.scala")(GenMatchers.genMain(_, scalaVersion.value)),
    sourceGenerators in Compile <+=
       (baseDirectory, sourceManaged in Compile) map genFiles("genfactories", "GenFactories.scala")(GenFactories.genMain(_, scalaVersion.value)),
    sourceGenerators in Compile <+=
       (baseDirectory, sourceManaged in Compile) map genFiles("gencompcls", "GenCompatibleClasses.scala")(GenCompatibleClasses.genMain(_, scalaVersion.value)),
    testOptions in Test := Seq(Tests.Argument("-l", "org.scalatest.tags.Slow",
                                             "-m", "org.scalatest",
                                             "-m", "org.scalautils",
                                             "-m", "org.scalatest.fixture",
                                             "-m", "org.scalatest.concurrent",
                                             "-m", "org.scalatest.testng",
                                             "-m", "org.scalatest.junit",
                                             "-m", "org.scalatest.events",
                                             "-m", "org.scalatest.prop",
                                             "-m", "org.scalatest.tools",
                                             "-m", "org.scalatest.matchers",
                                             "-m", "org.scalatest.suiteprop",
                                             "-m", "org.scalatest.mock",
                                             "-m", "org.scalatest.path",
                                             "-m", "org.scalatest.selenium",
                                             "-m", "org.scalatest.exceptions",
                                             "-m", "org.scalatest.time",
                                             "-m", "org.scalatest.words",
                                             "-m", "org.scalatest.enablers",
                                             "-oDI",
                                             "-h", "target/html",
                                             "-u", "target/junit",
                                             "-fW", "target/result.txt"))
   )

  def genSettings = coreSettings ++ generators ++ Seq(
    scalacOptions ++= Seq("-no-specialization", "-feature"),
    sourceGenerators in Test <+=
       (baseDirectory, sourceManaged in Test) map genFiles("gengen", "GenGen.scala")(GenGen.genTest(_, scalaVersion.value)),
    sourceGenerators in Test <+=
       (baseDirectory, sourceManaged in Test) map genFiles("gentables", "GenTable.scala")(GenTable.genTest(_, scalaVersion.value)),
    sourceGenerators in Test <+=
         (baseDirectory, sourceManaged in Test) map genFiles("genmatchers", "GenMatchers.scala")(GenMatchers.genTest(_, scalaVersion.value)),
    sourceGenerators in Test <+=
       (baseDirectory, sourceManaged in Test) map genFiles("genthey", "GenTheyWord.scala")(GenTheyWord.genTest(_, scalaVersion.value)),
    sourceGenerators in Test <+=
       (baseDirectory, sourceManaged in Test) map genFiles("geninspectors", "GenInspectors.scala")(GenInspectors.genTest(_, scalaVersion.value)),
    sourceGenerators in Test <+=
       (baseDirectory, sourceManaged in Test) map genFiles("gencontain", "GenContain.scala")(GenContain.genTest(_, scalaVersion.value)),
    sourceGenerators in Test <+=
       (baseDirectory, sourceManaged in Test) map genFiles("gensorted", "GenSorted.scala")(GenSorted.genTest(_, scalaVersion.value)),
    sourceGenerators in Test <+=
       (baseDirectory, sourceManaged in Test) map genFiles("genloneelement", "GenLoneElement.scala")(GenLoneElement.genTest(_, scalaVersion.value)),
    sourceGenerators in Test <+=
       (baseDirectory, sourceManaged in Test) map genFiles("genempty", "GenEmpty.scala")(GenEmpty.genTest(_, scalaVersion.value)),
    testOptions in Test := Seq(Tests.Argument("-l", "org.scalatest.tags.Slow",
                                             "-oDI",
                                             "-h", "gentests/target/html",
                                             "-u", "gentests/target/junit",
                                             "-fW", "target/result.txt"))
  )

  def genFiles(name: String, generatorSource: String)(gen: File => Unit)(basedir: File, outDir: File): Seq[File] = {
    val tdir = outDir / "scala" / name
    val genSource = basedir / "project" / generatorSource
    def results = (tdir ** "*.scala").get
    if (results.isEmpty || results.exists(_.lastModified < genSource.lastModified)) {
      tdir.mkdirs()
      gen(tdir)
    }
    results
  }

  def generators = Seq(
    genMustMatchers <<= (sourceManaged in Compile, sourceManaged in Test, name) map { (mainTargetDir: File, testTargetDir: File, projName: String) =>
      projName match {
        case "scalatest" =>
          GenMatchers.genMain(new File(mainTargetDir, "scala/genmatchers"), scalaVersion.value)
        case "gentests" =>
          GenMatchers.genTest(new File(testTargetDir, "scala/genmatchers"), scalaVersion.value)
      }
    },
    genGen <<= (sourceManaged in Compile, sourceManaged in Test, name) map { (mainTargetDir: File, testTargetDir: File, projName: String) =>
      projName match {
        case "scalatest" =>
          GenGen.genMain(new File(mainTargetDir, "scala/gengen"), scalaVersion.value)
        case "gentests" =>
          GenGen.genTest(new File(testTargetDir, "scala/gengen"), scalaVersion.value)
      }
    },
    genTables <<= (sourceManaged in Compile, sourceManaged in Test, name) map { (mainTargetDir: File, testTargetDir: File, projName: String) =>
      projName match {
        case "scalatest" =>
          GenTable.genMain(new File(mainTargetDir, "scala/gentables"), scalaVersion.value)
        case "gentests" =>
          GenTable.genTest(new File(testTargetDir, "scala/gentables"), scalaVersion.value)
      }
    },
    genTheyWord <<= (sourceManaged in Compile, sourceManaged in Test) map { (mainTargetDir: File, testTargetDir: File) =>
      GenTheyWord.genTest(new File(testTargetDir, "scala/genthey"), scalaVersion.value)
    },
    genInspectors <<= (sourceManaged in Compile, sourceManaged in Test) map { (mainTargetDir: File, testTargetDir: File) =>
      GenInspectors.genTest(new File(testTargetDir, "scala/geninspectors"), scalaVersion.value)
    },
    genFactories <<= (sourceManaged in Compile, sourceManaged in Test) map { (mainTargetDir: File, testTargetDir: File) =>
      GenFactories.genMain(new File(mainTargetDir, "scala/genfactories"), scalaVersion.value)
    },
    genCompatibleClasses <<= (sourceManaged in Compile, sourceManaged in Test) map { (mainTargetDir: File, testTargetDir: File) =>
      GenCompatibleClasses.genMain(new File(mainTargetDir, "scala/gencompclass"), scalaVersion.value)
    },
    genContain <<= (sourceManaged in Compile, sourceManaged in Test) map { (mainTargetDir: File, testTargetDir: File) =>
      GenContain.genTest(new File(testTargetDir, "scala/gencontain"), scalaVersion.value)
    },
    genSorted <<= (sourceManaged in Compile, sourceManaged in Test) map { (mainTargetDir: File, testTargetDir: File) =>
      GenSorted.genTest(new File(testTargetDir, "scala/gensorted"), scalaVersion.value)
    },
    genLoneElement <<= (sourceManaged in Compile, sourceManaged in Test) map { (mainTargetDir: File, testTargetDir: File) =>
      GenLoneElement.genTest(new File(testTargetDir, "scala/genloneelement"), scalaVersion.value)
    },
    genEmpty <<= (sourceManaged in Compile, sourceManaged in Test) map { (mainTargetDir: File, testTargetDir: File) =>
      GenEmpty.genTest(new File(testTargetDir, "scala/genempty"), scalaVersion.value)
    },
    genCode <<= (sourceManaged in Compile, sourceManaged in Test) map { (mainTargetDir: File, testTargetDir: File) =>
      GenGen.genMain(new File(mainTargetDir, "scala/gengen"), scalaVersion.value)
      GenTable.genMain(new File(mainTargetDir, "scala/gentables"), scalaVersion.value)
      GenMatchers.genMain(new File(mainTargetDir, "scala/genmatchers"), scalaVersion.value)
      GenFactories.genMain(new File(mainTargetDir, "scala/genfactories"), scalaVersion.value)
    }
  )
  
  lazy val genMustMatchers      = TaskKey[Unit]("genmatchers",    "Generate Must Matchers")
  lazy val genGen               = TaskKey[Unit]("gengen",         "Generate Property Checks")
  lazy val genTables            = TaskKey[Unit]("gentables",      "Generate Tables")
  lazy val genTheyWord          = TaskKey[Unit]("genthey",        "Generate They Word tests")
  lazy val genInspectors        = TaskKey[Unit]("geninspectors",  "Generate Inspectors tests")
  lazy val genFactories         = TaskKey[Unit]("genfactories",   "Generate Matcher Factories")
  lazy val genCompatibleClasses = TaskKey[Unit]("gencompcls",     "Generate Compatible Classes for Java 6 & 7")
  lazy val genContain           = TaskKey[Unit]("gencontain",     "Generate contain matcher tests")
  lazy val genSorted            = TaskKey[Unit]("gensorted",      "Generate sorted matcher tests")
  lazy val genLoneElement       = TaskKey[Unit]("genloneelement", "Generate lone element matcher tests")
  lazy val genEmpty             = TaskKey[Unit]("genempty",       "Generate empty matcher tests")
  lazy val genCode              = TaskKey[Unit]("gencode",        "Generate Code, includes Must Matchers and They Word tests.")
}
