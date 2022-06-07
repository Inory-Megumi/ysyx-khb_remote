package kcore

import chisel3._
import chisel3.util._

class IFU extends Module with InstConfig {
  val io = IO(new Bundle {
    val globalEn   = Input(Bool())
    //val nxtPC      = Flipped(new NXTPCIO)
    val if2bus      = new IF2BUSIO
    val if2id      = new IF2IDIO
  })
  protected val pc = RegInit(SimStartAddr)
  when(io.globalEn) {
    pc := pc + 4.U
  }

//communicate with memory
    io.if2bus.addr := pc
//output
    io.if2id.pc := pc
    io.if2id.inst := io.if2bus.data
}