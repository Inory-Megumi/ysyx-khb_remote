package kcore
import chisel3._
import chisel3.util._
class memory extends BlackBox with HasBlackBoxPath{
    val io = IO(new Bundle{
        val memread = Input(Bool())
        val memwrite = Input(Bool())
        val wmask = Input(UInt(8.W)) 
        val addr = Input(UInt(64.W))
        val rdata = Output(UInt(64.W))
        val wdata = Input(UInt(64.W))
    })
    addPath("playground/src/vsrc/memory.v")
}

class I_BUS extends Module with InstConfig{
    val io = IO(new Bundle{
        val ibus = Flipped(new IF2BUSIO)
    })
    val mem = Module(new memory)
    mem.io.memread := true.B
    mem.io.memwrite := false.B
    mem.io.addr := io.ibus.addr
    mem.io.wdata := 0.U
    mem.io.wmask := 0.U
    //pos decides instruction(32bit) location in bus data(64 bit) 
    val pos = io.ibus.addr(2)
    val inst = Mux(pos,mem.io.rdata(63,32),mem.io.rdata(31,0))
    io.ibus.data <> inst

}

class D_BUS extends Module with InstConfig{
    val io = IO(new Bundle{
        val dbus = Flipped(new MEM2BUSIO)
    })
    val mem = Module(new memory)
    mem.io.memread := io.dbus.memread 
    mem.io.memwrite := io.dbus.memwrite
    mem.io.addr := io.dbus.addr
    mem.io.wdata := io.dbus.wdata
    mem.io.wmask := "hff".U  //default
    mem.io.rdata <> io.dbus.rdata
}