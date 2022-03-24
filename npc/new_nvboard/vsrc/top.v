module top(
	input clk,
	input rst,
	input [1:0] sw,
	output [15:0] led
	);
	light t_light(clk,rst, led[15:2]);
	double_switch s_light(sw[1],sw[0],led[0]);
	assign led[1] = 0;
endmodule

