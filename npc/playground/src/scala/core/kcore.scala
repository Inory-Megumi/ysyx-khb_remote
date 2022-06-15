package kcore

import chisel3._
import chisel3.util._

class kcore extends Module with InstConfig{
    val io = IO(new Bundle{
        val usrio = new IF2IDIO
    })
    val IFU = Module(new IFU)
    val IDU = Module(new IDU)
    val EXU = Module(new EXU)
    val WBU = Module(new WBU)
    val MEM = Module(new MEM)
    //IFU
    IFU.io.globalEn := true.B
    IFU.io.mem2if := MEM.io.mem2if
    //IDU
    IDU.io.if2id := IFU.io.if2id
    IDU.io.wb2id := WBU.io.wb2id
    //EXU
    EXU.io.id2ex := IDU.io.id2ex
    //MEM
    MEM.io.ex2mem:= EXU.io.ex2mem
    //WBU
    WBU.io.mem2wb:= MEM.io.mem2wb
    io.usrio <> IFU.io.if2id
}