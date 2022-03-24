module light(
	input clk,
	input rst,
	output reg [15:2] led
);
	reg [31:0] count;
	always @(posedge clk) begin
		if (rst) begin led <= 1; count <= 0; end
		else begin
			if (count == 0) led <= {led[14:2], led[15]};
			count <= (count >= 500000? 32'b0 : count + 1);//500000time gap
			end 
		end
	endmodule
