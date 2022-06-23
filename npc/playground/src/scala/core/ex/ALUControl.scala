package kcore

import chisel3._
import chisel3.util._

class ALUControl extends Module with InstConfig{
    val io = IO(new Bundle{
        val aluop_in = Input(UInt(4.W))
        val aluoptype = Input(UInt(2.W))
        val aluop_out = Output(UInt(4.W))
        val zero_jump = Output(Bool())
        val btype = Output(Bool())
        val ritype = Input(Bool())
    })
    val zero_jump = Wire(Bool())
    io.zero_jump := zero_jump
    zero_jump := true.B
    io.aluop_out := AluOpADD
    io.btype := false.B
    switch(io.aluoptype){
        is("b00".U){
            io.aluop_out := AluOpADD
        }
        is("b01".U){
            //b type
            io.btype := true.B
            val funct3 = io.aluop_in(3,1)
            switch(funct3){
                is("b000".U) {//beq
                io.aluop_out := AluOpSUB
                }       
                is("b001".U) {//bne
                io.aluop_out := AluOpSUB
                zero_jump := false.B
                }
                is("b010".U) {//
                io.aluop_out := AluOpADD
                }  
                is("b011".U) {
                io.aluop_out := AluOpADD
                }  
                is("b100".U) {//blt
                io.aluop_out := AluOpSLT
                zero_jump := false.B
                }  
                is("b101".U) {//bge
                io.aluop_out := AluOpSLT
                }  
                is("b110".U) {//bltu
                io.aluop_out := AluOpSLTU
                zero_jump := false.B
                }  
                is("b111".U) {//bgeu
                io.aluop_out := AluOpSLTU
                }           
            }
        }
        is("b10".U){
            when( io.ritype && io.aluop_in(3,1) === "b000".U )
            {
            io.aluop_out := AluOpADD}
            .otherwise {
            io.aluop_out := io.aluop_in
            }
        }
        is("b11".U){
            io.aluop_out := AluOpADD
        }
    }
}