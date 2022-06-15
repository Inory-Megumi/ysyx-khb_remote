package kcore

import chisel3._
import chisel3.util._

class WB2IDIO extends Bundle with IOConfig {
  val wdata  = Output(UInt(XLen.W))
  val wreg   = Output(UInt(5.W))
  val regwrite = Output(Bool())
}
