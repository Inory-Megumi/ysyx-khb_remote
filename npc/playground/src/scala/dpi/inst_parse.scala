package kcore
import chisel3._
import chisel3.util._

class inst_parse extends BlackBox with HasBlackBoxPath{
    val io = IO(new Bundle{
        val inst = Input(UInt(32.W))
        val pc = Input(UInt(64.W))
    })
    addPath("playground/src/vsrc/inst_parse.v")
}
