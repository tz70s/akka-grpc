package akka.grpc.gen

class Options(optionsString: String) {
  val languageScala = optionsString.toLowerCase.contains("language=scala")
  val generateClient: Boolean = !optionsString.toLowerCase.contains("generate_client=false")
  val generateServer: Boolean = !optionsString.toLowerCase.contains("generate_server=false")
  val generatePlay: Boolean = optionsString.toLowerCase.contains("generate_play=true")
  val serverPowerApis: Boolean = optionsString.toLowerCase.contains("server_power_apis=true")
  val logfile: Option[String] = {
    val LogFileRegex = """(?:.*,)logfile=([^,]+)(?:,.*)?""".r
    optionsString match {
      case LogFileRegex(path) => Some(path)
      case _ => None
    }
  }
}
