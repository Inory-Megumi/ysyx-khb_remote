#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <memory>
#include <verilated.h>
#include "Vtop.h"
#include "verilated_vcd_c.h"
int main(int argc, char** argv, char** env){
	const std::unique_ptr<VerilatedContext> contextp{new VerilatedContext};
	contextp->debug(0);
	contextp->traceEverOn(true);
	contextp->commandArgs(argc, argv);
	const std::unique_ptr<Vtop> top{new Vtop{contextp.get(), "TOP"}};
	//vcd
	Verilated::mkdir("logs");
	VerilatedVcdC* tfp = new VerilatedVcdC;
	Verilated::traceEverOn(true);
	top->trace(tfp, 99);
	tfp->open("./logs/testbench.vcd");
	// initial input
	top->a = 0;
	top->b = 0;
	top->f = 0;
	//set simulation time
	int sim_time = 100;
while (contextp->time() < sim_time && !contextp->gotFinish()) {
	contextp->timeInc(1);
	int a = rand() & 1;
	int b = rand() & 1;
	top->a = a;
	top->b = b;
	top->eval();
	tfp->dump(contextp->time());
	printf("a = %d, b = %d, f = %d\n", a, b, top->f);
	assert(top->f == a ^ b);
}
	tfp->close();
	top->final();
#if VM_COVERAGE	
	Verilated::mkdir("logs");
	contextp->coveragep()->write("logs/coverage.dat");
#endif
	return 0;
}
