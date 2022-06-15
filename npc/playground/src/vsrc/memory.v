import "DPI-C" function void pmem_read(
  input longint raddr, output longint rdata);
import "DPI-C" function void pmem_write(
  input longint waddr, input longint wdata, input byte wmask);
module memory 
(
  input  [63:0] addr,
  input  memread,
  input  memwrite,
  output [63:0] rdata,
  input  [63:0] wdata,
  input  [7:0] wmask
  );
  always @(*) begin
    if(memread)
    pmem_read(addr, rdata);
    else if(memwrite)
    pmem_write(addr,wdata,wmask);
  end
endmodule

