package kcore

import chisel3._
import chisel3.util._

class IDU extends Module with InstConfig {
  val io = IO(new Bundle {
    val if2id = Flipped(new IF2IDIO)
    val wb2id = Flipped(new WB2IDIO)
    val id2ex = new ID2EXIO
  })
  protected val inst  = io.if2id.inst
  protected val rs1   = inst(19, 15)
  protected val rs2   = inst(24, 20)
  protected val rd    = inst(11, 7)
  protected val opcode= inst(6, 0)
  protected val controlunit = Module(new ControlUnit)
  protected val immExten    = Module(new ImmExten)
  protected val regfile = new RegFile
  /*              difftest                 */
  protected val difftest = Module(new difftest)
  difftest.io.gpr := regfile.gpr
  /*                end                    */
  io.id2ex.aluop := Cat(inst(14,12),inst(30))
  io.id2ex.wreg  := rd
  io.id2ex.pc_serial := io.if2id.pc_serial
  io.id2ex.pc := io.if2id.pc
  ////*                controlunit               *///////
  controlunit.io.opcode <> opcode
  io.id2ex.jtype := controlunit.io.cuio.jtype
  io.id2ex.memread := controlunit.io.cuio.memread
  io.id2ex.memtoreg:= controlunit.io.cuio.memtoreg
  io.id2ex.memwrite:= controlunit.io.cuio.memwrite
  io.id2ex.alusrc  := controlunit.io.cuio.alusrc
  io.id2ex.regwrite:= controlunit.io.cuio.regwrite
  io.id2ex.utype   := controlunit.io.cuio.utype
  io.id2ex.lui     := controlunit.io.cuio.lui
  io.id2ex.jalr    := controlunit.io.cuio.jalr
  io.id2ex.aluoptype := controlunit.io.aluop
  ////*                immeExten               *///////
  immExten.io.inst <> inst
  immExten.io.instType <> controlunit.io.immtype
  io.id2ex.imm := immExten.io.imm
  ////*                regfile               *///////
  io.id2ex.src1    := regfile.read(rs1)
  io.id2ex.src2    := regfile.read(rs2)
  val regwrite      = io.wb2id.regwrite
  //io.src2    := Mux(controlunit.io.cuio.alusrc,immExten.io.imm,regfile.read(rs2))
  //protected val wdata = Mux(controlunit.io.cuio.lui,immExten.io.imm,io.wdata)
  regfile.write(regwrite, io.wb2id.wreg, io.wb2id.wdata)
}