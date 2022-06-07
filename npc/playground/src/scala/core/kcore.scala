package kcore

import chisel3._
import chisel3.util._

class kcore extends Module with InstConfig{
    val io = IO(new Bundle{
        val usrio = new IF2IDIO
    })
    val IFU = Module(new IFU)
    val BUS = Module(new BUS)
    IFU.io.globalEn := true.B
    IFU.io.if2bus <> BUS.io.fetch
    io.usrio <> IFU.io.if2id
}