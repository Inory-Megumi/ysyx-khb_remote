package kcore

import chisel3._
import chisel3.util._

class MemControl extends Module with InstConfig {
  val io = IO(new Bundle {
    val addr    = Input(UInt(3.W))
    val data    = Input(UInt(XLen.W))
    val funct3  = Input(UInt(3.W))
    val data_out = Output(UInt(XLen.W))
    val wmask   = Output(UInt(8.W))
  })
  io.data_out := 0.U
  //could be optimized
  val shift = io.addr << 3.U
  val temp = io.data >> shift;// shift data according to addr
  switch{io.funct3}{
    is("b000".U) {//lb
        io.data_out := SignExt(temp(7,0),XLen)
                }       
    is("b001".U) {//lh
        io.data_out := SignExt(temp(15,0),XLen)
                }
    is("b010".U) {//lw
        io.data_out := SignExt(temp(31,0),XLen)
                }  
    is("b011".U) {//ld
        io.data_out := temp
                }  
    is("b100".U) {//lbu
        io.data_out := temp(7,0)
                }  
    is("b101".U) {//lhu
        io.data_out := temp(15,0)
                }  
  }
}
