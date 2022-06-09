package kcore

import chisel3._
import chisel3.util._

class BEU extends Module with InstConfig {
  val io = IO(new Bundle {
    val pc = Input(UInt(XLen.W))
    val alu_res = Input(UInt(XLen.W))
    val jalr = Input(Bool())
    val imm = Input(UInt(XLen.W))
    val nxtpc = Output(UInt(XLen.W))
    val pc_seq = Output(UInt(XLen.W))
    val pc_jmp = Output(UInt(XLen.W))
    val jtype = Input(Bool())
    val zero = Input(Bool()) 
    val zero_jump = Input(Bool())
    val btype = Input(Bool())
  })
  val bjump = io.zero & io.zero_jump && io.btype
  val jump = (io.jtype | bjump) 
  val nxtpc_seq = io.pc + 4.U
  val nxtpc_jmp = io.pc + io.imm 
  val nxtpc_tmp = Mux(jump,nxtpc_jmp,nxtpc_seq)
  io.nxtpc := Mux(io.jalr,io.alu_res,nxtpc_tmp)
  io.pc_seq := nxtpc_seq
  io.pc_jmp := nxtpc_jmp
}
