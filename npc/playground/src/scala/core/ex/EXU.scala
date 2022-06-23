package kcore
//ex top

import chisel3._
import chisel3.util._

class EXU extends Module with InstConfig {
  val io = IO(new Bundle {
    val id2ex = Flipped(new ID2EXIO)
    val ex2mem = new EX2MEMIO

    //aluctrl
    //val Branch = Input(Bool())
    //
  })
  protected val alu_ctrl = Module(new ALUControl)
  protected val alu      = Module(new ALU)
  protected val beu      = Module(new BEU)
  protected val src1  =  io.id2ex.src1
  protected val src2  =  Mux(io.id2ex.alusrc,io.id2ex.imm,io.id2ex.src2)
  /*             alu_ctrl           */
  alu_ctrl.io.aluop_in := io.id2ex.aluop
  alu_ctrl.io.aluoptype := io.id2ex.aluoptype
  alu_ctrl.io.ritype := io.id2ex.alusrc
  /*               alu               */
  alu.io.wtype := io.id2ex.wtype  //addiw
  alu.io.aluop := alu_ctrl.io.aluop_out
  alu.io.src1 := src1
  alu.io.src2 := src2
  /*              beu                */
  beu.io.pc := io.id2ex.pc
  beu.io.imm := io.id2ex.imm
  beu.io.jtype := io.id2ex.jtype
  beu.io.zero := alu.io.zero
  beu.io.zero_jump := alu_ctrl.io.zero_jump
  beu.io.btype := alu_ctrl.io.btype
//temporal  need to be changed pc+4 != nxtpc j type
  //ex2mem
  io.ex2mem.jalr  := io.id2ex.jalr
  io.ex2mem.branch:= beu.io.branch
  io.ex2mem.pc_branch:= beu.io.pc_jmp
  io.ex2mem.wdata := io.id2ex.src2
  io.ex2mem.waddr := alu.io.res
  io.ex2mem.pc_serial := io.id2ex.pc_serial
  io.ex2mem.imm   := io.id2ex.imm
  io.ex2mem.memread:= io.id2ex.memread
  io.ex2mem.memwrite := io.id2ex.memwrite
  io.ex2mem.memtoreg := io.id2ex.memtoreg
  io.ex2mem.regwrite := io.id2ex.regwrite
  io.ex2mem.utype    := io.id2ex.utype
  io.ex2mem.jtype    := io.id2ex.jtype
  io.ex2mem.lui      := io.id2ex.lui
  io.ex2mem.wreg     := io.id2ex.wreg
  io.ex2mem.funct3   := io.id2ex.aluop(3,1)
}