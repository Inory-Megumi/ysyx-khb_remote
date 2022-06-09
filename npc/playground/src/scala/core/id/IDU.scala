package kcore

import chisel3._
import chisel3.util._

class IDU extends Module with InstConfig {
  val io = IO(new Bundle {
    val inst = Input(UInt(InstLen.W))
    val wdata = Input(UInt(XLen.W))
    val aluop = Output(UInt(4.W))
    val src1 = Output(UInt(XLen.W))
    val src2 = Output(UInt(XLen.W))
    val imm  = Output(UInt(XLen.W))
    val aluoptype = Output(UInt(2.W))
    //cu
    val jtype = Output(Bool())
    val jalr = Output(Bool())
    val memread = Output(Bool())
    val memtoreg = Output(Bool())
    val memwrite = Output(Bool())
    val utype = Output(Bool())
  })
  protected val rs1   = io.inst(19, 15)
  protected val rs2   = io.inst(24, 20)
  protected val rd    = io.inst(11, 7)
  protected val opcode= io.inst(6, 0)
  protected val controlunit = Module(new ControlUnit)
  protected val immExten    = Module(new ImmExten)
  controlunit.io.opcode <> opcode
  immExten.io.inst <> io.inst
  immExten.io.instType <> controlunit.io.immtype
  protected val regfile = new RegFile
  protected val regwren = controlunit.io.cuio.regwrite
  io.src1    := regfile.read(rs1)
  io.src2    := Mux(controlunit.io.cuio.alusrc,immExten.io.imm,regfile.read(rs2))
  protected val wdata = Mux(controlunit.io.cuio.lui,immExten.io.imm,io.wdata)
  regfile.write(regwren, rd, wdata)
  io.jtype := controlunit.io.cuio.jtype
  io.memread := controlunit.io.cuio.memread
  io.memtoreg := controlunit.io.cuio.memtoreg
  io.memwrite := controlunit.io.cuio.memwrite
  io.utype    := controlunit.io.cuio.utype
  io.jalr    := controlunit.io.cuio.jalr
  io.aluop := Cat(io.inst(14,12),io.inst(30))
  io.aluoptype := controlunit.io.aluop
  io.imm := immExten.io.imm
}