/*
 * Copyright (C) 2019 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.grpc.gen

/**
 * Twirl code generation utilities.
 */
object TwirlGenUtil {

  sealed trait Language {
    def dsl(className: String): String

    def internalPrefix(className: String): String
  }

  case object Java extends Language {
    override def dsl(className: String): String = s"import akka.grpc.javadsl.$className;"

    override def internalPrefix(className: String): String = s"import akka.grpc.internal.$className;"
  }

  case object Scala extends Language {
    override def dsl(className: String): String = s"import akka.grpc.scaladsl.$className"

    override def internalPrefix(className: String): String = s"import akka.grpc.internal.$className"
  }

}

object GenImportRules {
  import TwirlGenUtil._

  sealed trait GenImportRules {

    def generate(language: Language): Set[String]
  }

  final case object UsePowerApis extends GenImportRules {

    override def generate(language: Language): Set[String] = {
      language match {
        case Scala => Set(language.dsl("MetadataImpl"))
        case Java  => Set(language.dsl("Metada"), language.dsl("MetadaImpl"))
      }
    }
  }

  final case class MethodTypeSignatures(methodType: MethodType) extends GenImportRules {

    override def generate(language: Language): Set[String] = {
      methodType match {
        case Unary => withSingleResponseBuilder(language.internalPrefix("UnaryRequestBuilder"), language)
        case ClientStreaming =>
          withSingleResponseBuilder(language.internalPrefix("ClientStreamingRequestBuilder"), language)
        case ServerStreaming =>
          withStreamResponseBuilder(language.internalPrefix("ServerStreamRequestBuilder"), language)
        case BidiStreaming =>
          withStreamResponseBuilder(language.internalPrefix("BidirectionalStreamingRequestBuilder"), language)
      }
    }

    private def withStreamResponseBuilder(stmt: String, language: Language) =
      Set(language.dsl("StreamResponseRequestBuilder"))
    private def withSingleResponseBuilder(stmt: String, language: Language) =
      Set(language.dsl("SingleResponseRequestBuilder"), stmt)
  }
}
