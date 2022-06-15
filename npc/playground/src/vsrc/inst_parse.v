import "DPI-C" function void inst_parse(
  input longint pc,input int inst);

module inst_parse(
  input [31:0] inst,
  input [63:0] pc
);
  always @(*) begin
    inst_parse(pc,inst);
  end
endmodule