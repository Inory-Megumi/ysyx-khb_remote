package kcore

import chisel3._
import chisel3.util._

class CUIO extends Bundle with IOConfig {
  val jtype   = Output(Bool())
  val memread  = Output(Bool())
  val memtoreg = Output(Bool())
  val memwrite = Output(Bool())
  val alusrc   = Output(Bool())
  val regwrite = Output(Bool())
  val utype    = Output(Bool())
  val lui      = Output(Bool())
  val jalr     = Output(Bool())
}
