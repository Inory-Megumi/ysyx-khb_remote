package kcore

import chisel3._
import chisel3.util._

class MEM2IFIO extends Bundle with IOConfig {
  val pc_serial = Output(UInt(XLen.W))
  val pc_branch = Output(UInt(XLen.W))
  val branch    = Output(Bool())
}
