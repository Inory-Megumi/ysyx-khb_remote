package kcore

import chisel3._
import chisel3.util._

class MEM2BUSIO extends Bundle with IOConfig {
  //val en   = Output(Bool())
  val addr = Output(UInt(XLen.W))
  val wdata = Output(UInt(XLen.W))
  val rdata = Input(UInt(XLen.W))
  val memread = Output(Bool())
  val memwrite = Output(Bool())
  val wmask = Output(UInt(8.W))
}
