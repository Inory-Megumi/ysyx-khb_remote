package kcore

import chisel3._
import chisel3.util._

class ALU extends Module with InstConfig {
  val io = IO(new Bundle {
    val aluop  = Input(UInt(AluOpLen.W))
    val src1 = Input(UInt(XLen.W))
    val src2 = Input(UInt(XLen.W))
    val res  = Output(UInt(XLen.W))
    val zero = Output(Bool())
  })
  io.res := MuxLookup(
    io.aluop,
    0.U(XLen.W),
    Seq(
      AluOpADD  -> (io.src1 + io.src2),
      AluOpSUB  -> (io.src1 - io.src2),
      AluOpSLL  -> (io.src1 << io.src2(5, 0))(63, 0),
      AluOpSLT   -> Mux(io.src1.asSInt < io.src2.asSInt, 1.U(XLen.W), 0.U(XLen.W)),
      AluOpSLTU -> Mux(io.src1.asUInt < io.src2.asUInt, 1.U(XLen.W), 0.U(XLen.W)),
      AluOpXOR  -> (io.src1 ^ io.src2),
      AluOpAND   -> (io.src1 & io.src2),
      AluOpOR    -> (io.src1 | io.src2),
      AluOpSRL   -> (io.src1 >> io.src2(5, 0)),
      AluOpSRA   -> (io.src1.asSInt >> io.src2(5, 0)).asUInt
    )
  )
  io.zero := Mux((io.res === 0.U),true.B,false.B)
}