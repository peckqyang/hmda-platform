import com.lightbend.cinnamon.sbt.Cinnamon
import com.typesafe.sbt.packager.docker._

version := "latest"

packageName in Docker := "hmda-platform"

dockerExposedPorts := Vector(8080, 8081, 8082, 19999, 9080, 1099)

dockerEntrypoint ++= Seq(
  """-Dakka.remote.netty.tcp.hostname="$(eval "echo $AKKA_REMOTING_BIND_HOST")"""",
  """-Dakka.management.http.hostname="$(eval "echo $AKKA_REMOTING_BIND_HOST")""""
)

dockerCommands :=
  dockerCommands.value.flatMap {
    case ExecCmd("ENTRYPOINT", args @ _*) =>
      Seq(Cmd("ENTRYPOINT", args.mkString(" ")))
    case v => Seq(v)
  }

credentials in ThisBuild += Credentials(Path.userHome / ".lightbend" / "commercial.credentials")
resolvers in ThisBuild += "lightbend-commercial-maven" at "https://repo.lightbend.com/commercial-releases"

cinnamonMuteMissingRepoWarning := true

cinnamonLogLevel := "INFO"

cinnamon in run := true
cinnamon in test := true

libraryDependencies += Cinnamon.library.cinnamonAkka
libraryDependencies += Cinnamon.library.cinnamonAkkaHttp
libraryDependencies += Cinnamon.library.cinnamonAkkaStream
libraryDependencies += Cinnamon.library.cinnamonPrometheus
libraryDependencies += Cinnamon.library.cinnamonPrometheusHttpServer
libraryDependencies += Cinnamon.library.cinnamonJvmMetricsProducer
libraryDependencies += Cinnamon.library.cinnamonAkkaPersistence
libraryDependencies += "com.lightbend.akka" %% "akka-diagnostics" % "1.1.12"

javaOptions in Universal ++= Seq(
  "-J-XX:+UnlockExperimentalVMOptions",
  "-J-XX:+UseCGroupMemoryLimitForHeap",
  "-J-XX:+UseContainerSupport",
  "-J-XX:+PrintGCDetails",
  "-J-XX:+PrintGCDateStamps",
//  "-J-XX:+UnlockDiagnosticVMOptions", //allow JFR to get metadata about non-safe points
//  "-J-XX:+DebugNonSafepoints", //allow JFR to get metadata about non-safe points
//  "-J-Xloggc:/opt/docker/gc.log",
//  "-J-Xlog:gc*",
  "-J-XX:+HeapDumpOnOutOfMemoryError",
  "-J-XX:HeapDumpPath=/dumps/oom.bin",
  "-J-XX:FlightRecorder",
//  "-J-XX:StartFlightRecording=disk=true, dumponexit=true, filename=recording.jfr, maxsize=2048m, maxage=1d, setting=profile",
  "-Dcom.sun.management.jmxremote",
  "-Dcom.sun.management.jmxremote.authenticate=false",
  "-Dcom.sun.management.jmxremote.ssl=false",
  "-Dcom.sun.management.jmxremote.local.only=false",
  "-Dcom.sun.management.jmxremote.port=1099",
  "-Dcom.sun.management.jmxremote.rmi.port=1099",
  "-Djava.rmi.server.hostname=127.0.0.1"
)

javaOptions in reStart ++= (javaOptions in run).value