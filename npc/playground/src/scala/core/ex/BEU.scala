package kcore

import chisel3._
import chisel3.util._

class BEU extends Module with InstConfig {
  val io = IO(new Bundle {
    val pc = Input(UInt(XLen.W))
    val branch = Output(Bool())
    val imm = Input(UInt(XLen.W))
    val pc_jmp = Output(UInt(XLen.W))
    val jtype = Input(Bool())
    val zero = Input(Bool()) 
    val zero_jump = Input(Bool())
    val btype = Input(Bool())
  })
  val bjump = io.zero & io.zero_jump && io.btype
  io.branch := (io.jtype | bjump) 
  val nxtpc_jmp = io.pc + io.imm 
  io.pc_jmp := nxtpc_jmp
}
