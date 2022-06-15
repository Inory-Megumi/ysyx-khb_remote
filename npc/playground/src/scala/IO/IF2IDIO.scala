package kcore

import chisel3._
import chisel3.util._

class IF2IDIO extends Bundle with IOConfig {
  val inst      = Output(UInt(InstLen.W))
  val pc_serial = Output(UInt(XLen.W))
  val pc        = Output(UInt(XLen.W))
}
