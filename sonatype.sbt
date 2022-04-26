publishTo := sonatypePublishToBundle.value
// For all Sonatype accounts created on or after February 2021
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"

// Your profile name of the sonatype account. The default is the same with the organization value
sonatypeProfileName := "com.linkedin.feathr"

// To sync with Maven central, you need to supply the following information:
publishMavenStyle := true

// Open-source license of your choice
licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))


// Project metadata
homepage := Some(url("https://github.com/linkedin/feathr"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/linkedin/feathr"),
    "scm:git@github.com:linkedin/feathr.git"
  )
)
developers := List(
  Developer(id="blee1234", name="Ben Lee", email="blee1@linkedin.com", url=url("https://github.com/blee1234"))
)