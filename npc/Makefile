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
SOC_VSRC_TOP := kcore

SOC_CSRC_HOME += ./playground/src/csrc
SOC_CSRC_LIB_HOME += #for future
SOC_CXXFILES      += $(shell find $(SOC_CSRC_HOME) -name "*.cpp" )
SOC_CXXHEADRS      += $(shell find $(SOC_CSRC_HOME) -name "*.h" )
SOC_VSRC_HOME += ./playground/src/vsrc
SOC_VSRC_TOP := kcore
SOC_VSRC_LIB_HOME +=
SOC_VXXFILES      += $(shell find $(SOC_VSRC_HOME) -name "*.v")
SOC_VSRC_INCLPATH += -I$(SOC_VSRC_HOME)
SOC_CSRC_INCLPATH += -I$(SOC_CSRC_HOME)


# VERILATOR_CFLAGS += -I ./playground/src/vsrc -wall --cc --exe --build --trace -MMD \
					-O3 --x-assign fast --x-initial -fast --noassert\
					-Wno-UNUSED -Wno-PINCONNECTEMPTY _Wno-UNDRIVEN */

GCC_FLAGS    += -LDFLAGS -ldl
SOC_CXXFLAGS += -std=c++11 -static -Wall $(SOC_CSRC_INCLPATH) -DDUMP_WAVE_FST
SOC_FLAGS += --cc --exe --build $(SOC_CXXFILES) $(SOC_VXXFILES) --top-module $(SOC_VSRC_TOP)
SOC_FLAGS    += --x-assign unique -O3 -CFLAGS "$(SOC_CXXFLAGS)"
SOC_FLAGS    += --trace-fst --assert --stats-vars --output-split 30000 --output-split-cfuncs 30000 
SOC_FLAGS    += --timescale "1ns/1ns" -Wno-fatal 
SOC_FLAGS += -o ../$(BUILD_DIR)/soc/emu 
RUN_FLAGS += --dump-wave -i
BIN_FILE = 
RUN_FLAGS += $(BIN_FILE)

build = $(BUILD_DIR)/soc/emu

 

$(build): $(SOC_VXXFILES) $(SOC_CXXFILES) $(SOC_CXXHEADRS)
	mkdir -p $(BUILD_DIR) $(BUILD_DIR)/soc 
	verilator $(SOC_FLAGS) $(GCC_FLAGS)

build: $(build)

run: $(build)
	@echo $(RUN_FLAGS)
	$(BUILD_DIR)/soc/emu $(RUN_FLAGS) 
sim:
	gtkwave soc.wave
####### clean target #######
cleanChisel:
	rm -rf $(CHISEL_BUILD_DIR) $(MILL_OUT_DIR)

cleanVerilator:
	rm -rf $(BUILD_DIR)

cleanAll:cleanChisel cleanVerilator

.PHONY: test verilog help compile bsp reformat checkformat cleanAll cleanChisel cleanVerilator
