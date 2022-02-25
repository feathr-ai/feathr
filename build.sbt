
ThisBuild / resolvers += Resolver.mavenLocal
ThisBuild / scalaVersion     := "2.12.15"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "feathr"

val localAndCloudDiffDependencies = Seq(
    "org.apache.spark" %% "spark-avro" % "3.1.2",
    "org.apache.spark" %% "spark-sql" % "3.1.2",
    "org.apache.spark" %% "spark-hive" % "3.1.2",
    "com.google.guava" % "guava" % "17.0",
    "org.apache.logging.log4j" % "log4j-core" % "2.17.1",
    "com.typesafe" % "config" % "1.3.2",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.5",
    "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "2.7.2",
    "org.apache.hadoop" % "hadoop-common" % "2.7.2",
    "org.apache.avro" % "avro" % "1.7.7",
    "org.apache.xbean" % "xbean-asm6-shaded" % "4.10",
)

val cloudProvidedDeps = localAndCloudDiffDependencies.map(x => x % "provided")


val localAndCloudCommonDependencies = Seq(
    "org.apache.spark" %% "spark-catalyst" % "3.1.2" % Test,
    "org.testng" % "testng" % "6.14.3" % Test,
    "org.mockito" % "mockito-core" % "3.1.0" % Test,
    "nl.jqno.equalsverifier" % "equalsverifier" % "3.1.12" % Test,
    "org.scalatest" %% "scalatest" % "3.0.0" % Test,
    "it.unimi.dsi" % "fastutil" % "8.1.1",
    "org.mvel" % "mvel2" % "2.2.7.Final",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.6.7.1",
    "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.6.5",
    "com.fasterxml.jackson.dataformat" % "jackson-dataformat-csv" % "2.4.4",
    "com.jasonclawson" % "jackson-dataformat-hocon" % "1.1.0",
    "com.redislabs" %% "spark-redis" % "2.6.0",
    "org.scalatest" %% "scalatest" % "3.0.0" % "test",
    "org.apache.xbean" % "xbean-asm6-shaded" % "4.10"

) // Common deps

// For azure
lazy val root = (project in file("."))
  .settings(
      name := "feathr",
      // To assemble, run sbt assembly -java-home /Library/Java/JavaVirtualMachines/jdk1.8.0_282-msft.jdk/Contents/Home
      assembly / mainClass := Some("com.linkedin.feathr.offline.job.FeatureJoinJob"),
      libraryDependencies ++= cloudProvidedDeps,
      libraryDependencies ++= localAndCloudCommonDependencies,
      libraryDependencies ++= Seq(
          "org.apache.spark" %% "spark-core" % "3.1.2" % "provided"
      )
  )

// If you want to build jar for feathr test, enable this and comment out root
//lazy val localCliJar = (project in file("."))
// .settings(
//     name := "feathr-cli",
//     // To assemble, run sbt assembly -java-home /Library/Java/JavaVirtualMachines/jdk1.8.0_282-msft.jdk/Contents/Home
//     assembly / mainClass := Some("com.linkedin.feathr.cli.FeatureExperimentEntryPoint"),
//     // assembly / mainClass := Some("com.linkedin.feathr.offline.job.FeatureJoinJob"),
//     libraryDependencies ++= localAndCloudDiffDependencies,
//     libraryDependencies ++= localAndCloudCommonDependencies,
//     libraryDependencies ++= Seq(
//         // See https://stackoverflow.com/questions/55923943/how-to-fix-unsupported-class-file-major-version-55-while-executing-org-apache
//         "org.apache.spark" %% "spark-core" % "3.1.2" exclude("org.apache.xbean","xbean-asm6-shaded")
//     )
// )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
// publishTo := Some(Resolver.file("file", new File("/Users/tmp")))

// To assembly with certain java version: sbt assembly -java-home "/Library/Java/JavaVirtualMachines/jdk1.8.0_282-msft.jdk/Contents/Home"
// To execute the jar: java -jar target/scala-2.12/feathr-assembly-0.1.0-SNAPSHOT.jar

assembly / assemblyMergeStrategy := {
    // See https://stackoverflow.com/questions/17265002/hadoop-no-filesystem-for-scheme-file
    // See https://stackoverflow.com/questions/62232209/classnotfoundexception-caused-by-java-lang-classnotfoundexception-csv-default
    case PathList("META-INF","services",xs @ _*) => MergeStrategy.filterDistinctLines // Added this
    case PathList("META-INF",xs @ _*) => MergeStrategy.discard
    case _ => MergeStrategy.first
}