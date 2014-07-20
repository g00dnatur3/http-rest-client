import bintray.Keys._

name := "http-rest-client"

organization := "g00dnatur3"

version := "1.0.11"

bintrayPublishSettings

repository in bintray := "java-utils"

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

bintrayOrganization in bintray := None

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.apache.httpcomponents" % "httpclient" % "4.3.4",
  "commons-io" % "commons-io" % "2.4",
  "org.slf4j" % "slf4j-api" % "1.7.7",
  "org.slf4j" % "slf4j-simple" % "1.7.7",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.1.2",
  "com.google.guava" % "guava" % "17.0" % "test",
  "com.novocode" % "junit-interface" % "0.9" % "test"
)

