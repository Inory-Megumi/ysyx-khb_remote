package kcore

import chisel3._
import chisel3.util._

class MEM2WBIO extends Bundle with IOConfig {
  val alu_res    = Output(UInt(XLen.W))
  val imm    = Output(UInt(XLen.W))
  val pc_branch = Output(UInt(XLen.W))
  val pc_serial = Output(UInt(XLen.W))
  val mem_data = Output(UInt(XLen.W))
  val regwrite = Output(Bool())
  val memtoreg = Output(Bool())
  val wreg     = Output(UInt(5.W))
  val utype    = Output(Bool())
  val jtype    = Output(Bool())
  val lui      = Output(Bool())
}
