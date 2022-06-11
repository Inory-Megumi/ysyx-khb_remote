VERILATOR = verilator
CHISEL_BUILD_DIR = ./chisel_build
MILL_OUT_DIR = ./out
CHISEL_TOP =  kcore
VSRC_HOME += ./playground/src/vsrc
verilog = ./playground/src/vsrc/$(CHISEL_TOP).v
###### chisel target ######
test:
	mill -i __.test

verilog:
	$(call git_commit, "generate verilog")
	mkdir -p $(CHISEL_BUILD_DIR)
	mill -i __.test.runMain top.Elaborate -td $(CHISEL_BUILD_DIR)
ifeq ($(CHISEL_TOP), kcore)
	cp $(CHISEL_BUILD_DIR)/$(CHISEL_TOP).v $(VSRC_HOME)/$(CHISEL_TOP).v
endif
	@echo "verilog generated"


help:
	mill -i __.test.runMain top.Elaborate --help

compile:
	mill -i __.compile

bsp:
	mill -i mill.bsp.BSP/install

reformat:
	mill -i __.reformat

checkformat:
	mill -i __.checkFormat


###### verilator target ######
BUILD_DIR := ./build
VERILATOR = verilator
VSRC_TOP := kcore

CSRC_HOME += ./playground/src/csrc
CSRC_LIB_HOME += #for future
CXXHEADRS      += $(foreach dir, $(CSRC_HOME), $(wildcard $(dir)/*.h))
VSRC_TOP := kcore
SOC_VSRC_LIB_HOME +=
VSRC = $(shell find $(VSRC_HOME) -name "*.v")
CSRC += $(shell find $(CSRC_HOME) -name "*.cpp")
CXXHEADRS  += $(foreach dir, $(CSRC_HOME), $(wildcard $(dir)/*.h))


# VERILATOR_CFLAGS += -I ./playground/src/vsrc -wall --cc --exe --build --trace -MMD \
					-O3 --x-assign fast --x-initial -fast --noassert\
					-Wno-UNUSED -Wno-PINCONNECTEMPTY _Wno-UNDRIVEN */

GCC_FLAGS    += --LDFLAGS "-ldl $(shell llvm-config --cxxflags) -fPIE $(shell llvm-config --libs)"
SOC_CXXFLAGS +=  -std=c++11 -static -Wall $(SOC_CSRC_INCLPATH) -DDUMP_WAVE_FST
SOC_FLAGS += --cc --exe --build $(SOC_CXXFILES) $(SOC_VXXFILES) --top-module $(SOC_VSRC_TOP)
SOC_FLAGS    += --x-assign unique -O3 -CFLAGS "$(SOC_CXXFLAGS)"
SOC_FLAGS    += --trace-fst --assert --stats-vars --output-split 30000 --output-split-cfuncs 30000 
SOC_FLAGS    += --timescale "1ns/1ns" -Wno-fatal 
SOC_FLAGS += -o ../$(BUILD_DIR)/soc/emu 
#RUN FLAGS
RUN_FLAGS += --dump-wave -i
BIN_FILE = 
RUN_FLAGS += $(BIN_FILE)

build = $(BUILD_DIR)/soc/emu
VERILATOR_FLAGS = --trace --cc --exe $(CSRC) $(VSRC) --top-module $(VSRC_TOP) -Wno-fatal
CONFIG_FLAGS = -DCONFIG_VCD -DCONFIG_ITRACE
CFLAGS =-CFLAGS "-fpermissive -I $$NPC_HOME/playground/src/csrc/include -I /usr/lib/llvm-12/include $(CONFIG_FLAGS)" 
LDFLAGS = -LDFLAGS "$(shell llvm-config --libs) $(shell llvm-config --ldflags)  -ldl" 


$(build): $(VSRC) $(CSRC) $(CXXHEADRS)
	@echo $(VSRC)
	$(VERILATOR)  $(VERILATOR_FLAGS) $(CFLAGS) $(LDFLAGS)
	make -j -C ./obj_dir/ -f V$(VSRC_TOP).mk V$(VSRC_TOP) 

build: $(build)

run: $(build)
	@echo $(RUN_FLAGS)
	./obj_dir/V$(VSRC_TOP) $(BIN_FILE) 

sim:
	gtkwave waveform.vcd
####### clean target #######
cleanChisel:
	rm -rf $(CHISEL_BUILD_DIR) $(MILL_OUT_DIR)

cleanVerilator:
	rm -rf $(BUILD_DIR) obj_dir

cleanAll:cleanChisel cleanVerilator

.PHONY: test verilog help compile bsp reformat checkformat cleanAll cleanChisel cleanVerilator
