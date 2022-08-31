addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "1.1.0")

/**
 * Helps us publish the artifacts to sonatype, which in turn
 * pushes to maven central.
 *
 * https://github.com/xerial/sbt-sonatype/releases
 */
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.5") //https://github.com/xerial/sbt-sonatype/releases

/**
 *
 * Signs all the jars, used in conjunction with sbt-sonatype.
 *
 * https://github.com/sbt/sbt-pgp/releases
 */
addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.1.2")

/*
This is an sbt plugin to help automate releases to Sonatype and Maven Central from GitHub Actions.
https://github.com/sbt/sbt-ci-release
*/
addSbtPlugin("com.github.sbt" % "sbt-ci-release" % "1.5.10")

/**
 *
 * Supports more advanced dependency tree scripts
 * 
 * ex.
 * sbt dependencyTree -java-home /Library/Java/JavaVirtualMachines/jdk1.8.0_282-msft.jdk/Contents/Home
 * https://www.baeldung.com/scala/sbt-dependency-tree
 */
addDependencyTreePlugin
