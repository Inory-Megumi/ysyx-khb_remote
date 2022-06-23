package kcore

import chisel3._
import chisel3.util._

class EX2MEMIO extends Bundle with IOConfig {
  val wdata    = Output(UInt(XLen.W))//rs2
  val waddr    = Output(UInt(XLen.W))//alu_res
  val pc_branch = Output(UInt(XLen.W))
  val pc_serial = Output(UInt(XLen.W))
  val imm = Output(UInt(XLen.W))
  val memread = Output(Bool())
  val memwrite = Output(Bool())
  val memtoreg = Output(Bool())
  val regwrite = Output(Bool())
  val jtype    = Output(Bool())
  val utype    = Output(Bool())
  val lui      = Output(Bool())
  val wreg     = Output(UInt(5.W))
  //attention
  val branch   = Output(Bool())
  val jalr     = Output(Bool())
  val funct3   = Output(UInt(3.W))
}
