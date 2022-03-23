#setup variables



VERILATOR_INPUT = ./vsrc/top.v  ./csrc/sim_main.cpp 
Configuration = -Os -x-assign 0 -Wall --trace --assert --coverage
top = top
############################################################

all:
	@echo "Write this Makefile by your self."

sim:
	$(call git_commit, "sim RTL") # DO NOT REMOVE THIS LINE!!!
	verilator -cc --exe $(Configuration) --build -j $(VERILATOR_INPUT)
	obj_dir/V$(top)
sim_nvboard:



clean:
	-rm -rf obj_dir logs *.log *.dmp *.vpd core
	
include ../Makefile
