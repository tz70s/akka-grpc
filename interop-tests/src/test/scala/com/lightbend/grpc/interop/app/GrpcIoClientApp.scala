package com.lightbend.grpc.interop.app

import io.grpc.testing.integration.EmptyProtos.Empty
import io.grpc.testing.integration.TestServiceGrpc
import io.grpc.testing.integration2.{ChannelBuilder, Settings}

object GrpcIoClientApp extends App {
  val channel = ChannelBuilder.buildChannel(Settings(
    "localhost",
    "localhost",
    9000,
    "x",
    false,
    false,
    false,
    "","", ""
  ))

  val blockingStub = TestServiceGrpc.newBlockingStub(channel)
  println(blockingStub.emptyCall(Empty.getDefaultInstance))
}
