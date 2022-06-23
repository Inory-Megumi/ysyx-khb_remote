
package kcore

import chisel3._
import chisel3.util._

class MEM extends Module with InstConfig {
  val io = IO(new Bundle {
    val ex2mem = Flipped(new EX2MEMIO)
    val mem2wb = new MEM2WBIO
    val mem2if = new MEM2IFIO
  })
  val datamem = Module(new D_BUS)
  val memControl = Module(new MemControl)
  io.mem2wb.alu_res := io.ex2mem.waddr
  io.mem2wb.imm := io.ex2mem.imm
  io.mem2wb.pc_branch := io.ex2mem.pc_branch
  io.mem2wb.pc_serial := io.ex2mem.pc_serial
  io.mem2wb.memtoreg  := io.ex2mem.memtoreg
  io.mem2wb.regwrite  := io.ex2mem.regwrite
  io.mem2wb.wreg      := io.ex2mem.wreg
  io.mem2wb.utype     := io.ex2mem.utype
  io.mem2wb.jtype     := io.ex2mem.jtype
  io.mem2wb.lui       := io.ex2mem.lui
//mem2if
  io.mem2if.pc_branch := Mux(io.ex2mem.jalr,io.ex2mem.waddr,io.ex2mem.pc_branch)//jalr
  io.mem2if.pc_serial := io.ex2mem.pc_serial
  io.mem2if.branch    := io.ex2mem.branch
/*datamem*/
  datamem.io.dbus.addr    := io.ex2mem.waddr //waddr - alu_res
  datamem.io.dbus.wdata   := io.ex2mem.wdata //rs2
  datamem.io.dbus.memread   :=  io.ex2mem.memread
  datamem.io.dbus.memwrite  :=  io.ex2mem.memwrite
  
  
/*MemControl*/
  memControl.io.addr := io.ex2mem.waddr(2,0)
  memControl.io.data := Mux(io.ex2mem.memwrite,io.ex2mem.wdata,datamem.io.dbus.rdata)
  memControl.io.funct3 := io.ex2mem.funct3
  memControl.io.data_out <> io.mem2wb.mem_data
  memControl.io.wmask <> datamem.io.dbus.wmask
}