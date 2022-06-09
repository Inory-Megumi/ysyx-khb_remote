package kcore

import chisel3._
import chisel3.util._

class kcore extends Module with InstConfig{
    val io = IO(new Bundle{
        val usrio = new IF2IDIO
    })
    
    val IFU = Module(new IFU)
    val BUS = Module(new BUS)
    val IDU = Module(new IDU)
    val EXU = Module(new EXU)
    //IFU
    IFU.io.globalEn := true.B
    IFU.io.nxtPC := EXU.io.nxtpc
    IFU.io.if2bus <> BUS.io.fetch
    io.usrio <> IFU.io.if2id
    //BUS
    
    //IDU
    IDU.io.inst := IFU.io.if2id.inst
    IDU.io.wdata := EXU.io.res
    //EXU
    EXU.io.pc := IFU.io.if2id.pc
    EXU.io.src1 := IDU.io.src1
    EXU.io.src2 := IDU.io.src2
    EXU.io.imm := IDU.io.imm
    EXU.io.aluop_in := IDU.io.aluop
    EXU.io.aluoptype := IDU.io.aluoptype
    EXU.io.utype := IDU.io.utype
    EXU.io.jtype := IDU.io.jtype
    EXU.io.jalr := IDU.io.jalr
}