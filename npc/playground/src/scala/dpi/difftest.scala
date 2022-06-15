package kcore
import chisel3._
import chisel3.util._

class difftest extends BlackBox with HasBlackBoxPath{
    val io = IO(new Bundle{
        val gpr = Input(Vec(32, UInt(64.W)))
    })
    addPath("playground/src/vsrc/difftest.v")
}