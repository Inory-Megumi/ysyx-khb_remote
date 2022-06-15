package kcore

import chisel3._
import chisel3.util._

class WBU extends Module with InstConfig {
  val io = IO(new Bundle {
    val mem2wb = Flipped(new MEM2WBIO)
    val wb2id = new WB2IDIO
  })
  io.wb2id.wreg := io.mem2wb.wreg
  io.wb2id.regwrite := io.mem2wb.regwrite
  val wdata_0 = Mux(io.mem2wb.memtoreg,io.mem2wb.mem_data,io.mem2wb.alu_res)
  val wdata_1 = Mux(io.mem2wb.jtype,io.mem2wb.pc_serial,wdata_0)
  val wdata_2 = Mux(io.mem2wb.lui,io.mem2wb.imm,io.mem2wb.pc_branch)
  val wdata_3 = Mux(io.mem2wb.utype,wdata_2,wdata_1)
  io.wb2id.wdata := wdata_3
}

