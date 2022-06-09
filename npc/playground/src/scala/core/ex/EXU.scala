package kcore
//ex top

import chisel3._
import chisel3.util._

class EXU extends Module with InstConfig {
  val io = IO(new Bundle {
    val pc = Input(UInt(XLen.W))
    val src1 = Input(UInt(XLen.W))
    val src2 = Input(UInt(XLen.W))
    val imm  = Input(UInt(XLen.W))
    //aluctrl
    val aluop_in = Input(Bool())
    val aluoptype =  Input(UInt(2.W))
    //val Branch = Input(Bool())
    //
    val jalr = Input(Bool())
    val utype = Input(Bool())
    val jtype = Input(Bool())
    val res = Output(UInt(XLen.W))
    val nxtpc = Output(UInt(XLen.W))
  })
  protected val alu_ctrl = Module(new ALUControl)
  protected val alu      = Module(new ALU)
  protected val beu      = Module(new BEU)
  alu_ctrl.io.aluop_in := io.aluop_in
  alu_ctrl.io.aluoptype := io.aluoptype
  alu.io.aluop := alu_ctrl.io.aluop_out
  alu.io.src1 := io.src1
  alu.io.src2 := io.src2
  beu.io.pc := io.pc   
  beu.io.imm := io.imm 
  beu.io.alu_res := alu.io.res
  beu.io.jalr := io.jalr
  beu.io.jtype := io.jtype
  beu.io.zero := alu.io.zero
  beu.io.zero_jump := alu_ctrl.io.zero_jump
  beu.io.btype := alu_ctrl.io.btype
  io.nxtpc := beu.io.nxtpc
//temporal  need to be changed pc+4 != nxtpc j type
  io.res := Mux(io.jtype,beu.io.pc_seq,Mux(io.utype,beu.io.pc_jmp,alu.io.res))
}