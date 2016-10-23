name := """macrocoupon-rest-scala"""
version := "1.0-SNAPSHOT"
lazy val root = (project in file(".")).enablePlugins(PlayScala).dependsOn(swagger)
lazy val swagger = RootProject(uri("https://github.com/CreditCardsCom/swagger-play.git"))
scalaVersion := "2.11.7"

scalacOptions ++= Seq("-feature", "-deprecation", "-unchecked", "-language:reflectiveCalls", "-language:postfixOps", "-language:implicitConversions")

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

doc in Compile <<= target.map(_ / "none")

scalariformSettings
libraryDependencies += evolutions
libraryDependencies += filters
libraryDependencies ++= Seq(
  specs2 % Test,
  jdbc,
  ws,

  "org.specs2"                   %% "specs2-matcher-extra"         % "3.8.5"                     % Test,
  "io.swagger"                   %% "swagger-play2"                % "1.5.1",

  "org.webjars"                  % "swagger-ui"                    % "2.1.8-M1",

  "org.elasticsearch"            % "elasticsearch"                 % "1.7.2",
  "com.paypal.sdk"               % "rest-api-sdk"                  % "1.4.1",

  "com.typesafe.play"            %% "play-slick"                   % "2.0.0",
  "org.slf4j"                    % "slf4j-nop"                     % "1.6.4",
  "org.postgresql"               % "postgresql"                    % "9.4-1206-jdbc42",

  "joda-time"                    % "joda-time"                     % "2.9.4",
  "org.joda"                     % "joda-convert"                  % "1.8.1",
  "com.github.tototoshi"         % "slick-joda-mapper_2.11"        % "2.2.0",

  "net.glxn"                     % "qrgen"                         % "1.4",
  "org.clapper"                  %% "grizzled-scala"               % "2.5.0",


  "com.typesafe.play"             % "play-mailer_2.11"             % "5.0.0",

  "org.scalatestplus.play"       %% "scalatestplus-play"           % "1.5.0"                      % "test",
  "org.scalatest"                %% "scalatest"                    % "3.0.0"                      % "test"

)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
resolvers += "QRGen" at "http://kenglxn.github.com/QRGen/repository"
resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

