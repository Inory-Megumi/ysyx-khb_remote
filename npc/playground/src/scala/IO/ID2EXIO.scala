package kcore

import chisel3._
import chisel3.util._

class ID2EXIO extends Bundle with IOConfig {
  val src1     = Output(UInt(XLen.W))
  val src2     = Output(UInt(XLen.W))//DATA MAYBE WRITTEN 
  val imm      = Output(UInt(XLen.W))
  val aluop    = Output(UInt(4.W))
  val aluoptype= Output(UInt(2.W))
  val pc       = Output(UInt(XLen.W))
  val pc_serial = Output(UInt(XLen.W))
  val wreg     = Output(UInt(5.W))
//Control unit
  val jtype   = Output(Bool())
  val memread  = Output(Bool())
  val memtoreg = Output(Bool())
  val memwrite = Output(Bool())
  val alusrc   = Output(Bool())
  val regwrite = Output(Bool())
  val utype    = Output(Bool())
  val lui      = Output(Bool())
  val jalr     = Output(Bool())
  val wtype    = Output(Bool())
}

 