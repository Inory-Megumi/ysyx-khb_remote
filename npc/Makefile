#setup variables
VSRCS = ./vsrc/top.v  
CSRCS = ./csrc/sim_main.cpp 
Configuration = -Os -x-assign 0 -Wall --trace --assert --coverage
TOPNAME = top
VCD_FILES = ./logs/testbench.vcd
############################################################
default:
	verilator -cc --exe $(Configuration) --build -j $(VSRCS) $(CSRCS)
	@echo "vcd files dumped"
all:
	default
sim:
	$(call git_commit, "sim RTL") # DO NOT REMOVE THIS LINE!!!
	@echo "*****GTKWAVES RUNNING*****"
	gtkwave $(VCD_FILES)
run:
	@echo "*****running******"
	obj_dir/V$(TOPNAME)
clean:
	-rm -rf obj_dir  logs *.log *.dmp *.vpd core
	
include ../Makefile
