package kcore

import chisel3._
import chisel3.util._
object OPCODEDecoder {
  // according to the The RISC-V Instruction Set Manual Volume I: Unprivileged ISA
  // Document Version: 20191213
  // DESC page [15-25] ABI page[130-131]
  /* Integer Register-Immediate Instructions */
  // I type inst
  //ADDI -- ADDIW
  def ITYPE0  = BitPat("b0010011")
  def ITYPE1 = BitPat("b0011011")

  // U type inst
  // LUI - rd = {imm, 12b'0}
  def UTYPE0 = BitPat("b0110111")
  //  - rd = PC + {imm, 12b'0}
  def UTYPE1 = BitPat("b0010111")

  /* Integer Register-Register Operations */
  // R type inst
  // ADD - ADDW
  def RTYPE0  = BitPat("b0110011")
  def RTYPE1 = BitPat("b0111011")

  /* Control Transfer Instructions */
  // J type inst
  def JTYPE = BitPat("b1101111")
  // I type inst
  def ITYPE2 = BitPat("b1100111")
  // B type inst
  def BTYPE  = BitPat("b1100011")

  /* Load and Store Instructions */
  // I type inst
  def LTYPE  = BitPat("b0000011")

  // S type inst
  def STYPE = BitPat("b0100011")

  // CSR inst
  //def CSRTYPE  = BitPat("b1110011")

  // system inst
//  def SYSTYPE0    = BitPat("b1110011")
  //def SYSTYPE1    = BitPat("b0001111")
}  
class ControlUnit extends Module with InstConfig {
  val io = IO(new Bundle {
    val opcode = Input(UInt(7.W))
    val cuio = new CUIO
    val immtype = Output(UInt(InstTypeLen.W))
    val aluop = Output(UInt(2.W))
  })
  protected val decodeTable = Array(
    //list structure(InstType,jtype,memread,memtoreg,memwrite,alusrc,regwrite,utype,lui,aluop,jalr)
    // aluop define : 00 : s/l/j  01 : arithmetic 10 : b
    OPCODEDecoder.ITYPE0 -> List(iInstType,false.B,false.B,false.B,false.B,true.B,true.B,false.B,false.B,"b10".U,false.B),
    OPCODEDecoder.ITYPE1 -> List(iInstType,false.B,false.B,false.B,false.B,true.B,true.B,false.B,false.B,"b10".U,false.B),
    OPCODEDecoder.UTYPE0 -> List(uInstType,false.B,false.B,false.B,false.B,false.B,true.B,true.B,true.B,"b00".U,false.B),
    OPCODEDecoder.UTYPE1 -> List(uInstType,false.B,false.B,false.B,false.B,false.B,true.B,true.B,false.B,"b00".U,false.B), 
    OPCODEDecoder.RTYPE0 -> List(rInstType,false.B,false.B,false.B,false.B,false.B,true.B,false.B,false.B,"b10".U,false.B),  
    OPCODEDecoder.RTYPE1 -> List(rInstType,false.B,false.B,false.B,false.B,false.B,true.B,false.B,false.B,"b10".U,false.B), 
    OPCODEDecoder.JTYPE  -> List(jInstType,true.B,false.B,false.B,false.B,false.B,true.B,false.B,false.B,"b00".U,false.B),
    OPCODEDecoder.ITYPE2 -> List(iInstType,true.B,false.B,false.B,false.B,true.B,true.B,false.B,false.B,"b00".U,true.B),
    OPCODEDecoder.LTYPE  -> List(iInstType,false.B,true.B,true.B,false.B,true.B,true.B,false.B,false.B,"b00".U,false.B),
    OPCODEDecoder.STYPE  -> List(sInstType,false.B,false.B,false.B,true.B,true.B,false.B,false.B,false.B,"b00".U,false.B),
    OPCODEDecoder.BTYPE  -> List(bInstType,false.B,false.B,false.B,false.B,false.B,false.B,false.B,false.B,"b01".U,false.B)
    )
    protected val defRes   = List(iInstType,false.B,false.B,false.B,false.B,true.B,false.B,false.B,false.B,"b00".U,false.B)
    protected val decRes   = ListLookup(io.opcode, defRes, decodeTable)//decode
    io.cuio.jtype := decRes(1)
    io.cuio.memread := decRes(2)
    io.cuio.memtoreg := decRes(3)
    io.cuio.memwrite := decRes(4)
    io.cuio.alusrc := decRes(5)
    io.cuio.regwrite := decRes(6)
    io.cuio.utype := decRes(7)
    io.cuio.lui := decRes(8)
    io.immtype := decRes(0)
    io.aluop := decRes(9)
    io.cuio.jalr := decRes(10)
    }
