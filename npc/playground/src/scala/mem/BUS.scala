package kcore
import chisel3._
import chisel3.util._
class memory extends BlackBox with HasBlackBoxPath{
    val io = IO(new Bundle{
        val raddr = Input(UInt(64.W))
        val rdata = Output(UInt(64.W))
        val inst = Input(UInt(32.W))
    })
    addPath("playground/src/vsrc/memory.v")
}

class BUS extends Module with InstConfig{
    val io = IO(new Bundle{
        val fetch = Flipped(new IF2BUSIO)
    })
    val mem = Module(new memory)
    io.fetch.addr <> mem.io.raddr
    //pos decides instruction(32bit) location in bus data(64 bit) 
    val pos = mem.io.raddr(2)
    val inst = Mux(pos,mem.io.rdata(63,32),mem.io.rdata(31,0))
    io.fetch.data <> inst
    inst <> mem.io.inst
}