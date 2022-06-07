package kcore

import chisel3._
import chisel3.util._

class IF2BUSIO extends Bundle with IOConfig {
  //val en   = Output(Bool())
  val addr = Output(UInt(XLen.W))
  val data = Input(UInt(InstLen.W))
}
