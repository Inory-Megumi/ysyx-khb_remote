package kcore

import chisel3._
import chisel3.util._

class IFU extends Module with InstConfig {
  val io = IO(new Bundle {
    val globalEn   = Input(Bool())
    val if2id      = new IF2IDIO
    val mem2if     = Flipped(new MEM2IFIO)
  })
  protected val pc = RegInit(SimStartAddr)
  protected val branch = io.mem2if.branch
  when(io.globalEn) {
    when(branch === true.B){
      pc := io.mem2if.pc_branch}
    .otherwise{
      pc := io.mem2if.pc_serial
  }
}
    val pc_serial = pc + 4.U;
//if2bus
  val instmem = Module(new I_BUS)
  instmem.io.ibus.addr := pc
//if2id
    io.if2id.pc := pc
    io.if2id.pc_serial := pc_serial
    io.if2id.inst := instmem.io.ibus.data
//dpic inst parse
  protected val inst_parse = Module(new inst_parse)
  inst_parse.io.pc := pc
  inst_parse.io.inst := instmem.io.ibus.data
}