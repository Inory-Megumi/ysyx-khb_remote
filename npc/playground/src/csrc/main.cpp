#include "npc.h"

CPU_state cpu;
vluint64_t sim_time = 0;
Vkcore *top = nullptr;
VerilatedContext *contextp = nullptr;
long img_size = 0;
char *img_file = nullptr;
#ifdef CONFIG_VCD
VerilatedVcdC *vcd_ptr = nullptr;
#endif
#ifdef CONFIG_ITRACE
char itrace_buf[16][100] = {0};
int itrace_buf_cnt = 0;
#endif
long ld(char *img_file);
void disassemble(char *str, int size, uint64_t pc, uint8_t *code, int nbyte);
void init_disasm(const char *triple);
void check_state();
#ifdef CONFIG_ITRACE
void print_itrace()
{
  puts("itrace:");
  for (int i = 0; i < 16; i++)
  {
    if (strlen(itrace_buf[i]) == 0)
      break;
    if ((i + 1) % 16 == itrace_buf_cnt)
      printf("-->");
    else
      printf("   ");
    printf("%s\n", itrace_buf[i]);
  }
}
#endif
void exit_npc()
{
#ifdef CONFIG_VCD
  vcd_ptr->close();
  printf("vcd files dumped\n");
#endif
  delete top;
  delete contextp;
}
/*  simulation    */
void reset()
{
    for (int i = 1; i <= 10; i++)
    {
        top->clock = 0, top->eval();
        top->clock = 1, top->eval();
    }
    top->clock = 0;
    top->reset = 0;
    top->eval();
}
void cpu_sim_once()
{//1 period
  top->clock = 0, top->eval();
  #ifdef CONFIG_VCD
  vcd_ptr->dump(sim_time++);
    #endif
  top->clock = 1, top->eval();
  #ifdef CONFIG_VCD
  vcd_ptr->dump(sim_time++);
    #endif
}
void exec_once()
{
  cpu_sim_once();
  printf("0x%08lx: 0x%08x\n",cpu.pc,cpu.inst);
#ifdef CONFIG_ITRACE
  char p[100] = {0};
  disassemble(p, 100, cpu.pc, (uint8_t *)&(cpu.inst), 4);
  sprintf(itrace_buf[itrace_buf_cnt], "pc=0x%016lx inst=%08x %s", cpu.pc, cpu.inst, p);
  printf("%s\n",itrace_buf[itrace_buf_cnt]);
  itrace_buf_cnt++;
  itrace_buf_cnt %= 16;
#endif
/*
#ifdef CONFIG_VCD
  vcd_ptr->dump(sim_time++);
#endif */
#ifdef CONFIG_DIFFTEST
  ref_difftest_exec(1);
  CPU_state ref_cpu;
  ref_difftest_regcpy(&ref_cpu, DIFFTEST_TO_DUT);
  printf("check at nemu_pc=%lx, npc_pc=%lx\n", cpu_npc.pc, ref_cpu.pc);
  if (!check_regs_npc(ref_cpu))
    exit_npc(-1);
#endif
  check_state();
}
void init_npc()
{
  top->reset = 1;
  reset();
  cpu.state = true;
  printf("0x%08lx: 0x%08x\n",cpu.pc,cpu.inst);
}

static int parse_args(int argc, char *argv[])
{
  if (argc == 2)
  {
    if (strlen(argv[1]) != 0)
    {
      img_file = argv[1];
      img_size = ld(img_file);
    }
  }
  return 0;
}

int main(int argc, char **argv, char **env)
{
  parse_args(argc, argv);

  contextp = new VerilatedContext;
  contextp->commandArgs(argc, argv);
  top = new Vkcore{contextp};

#ifdef CONFIG_ITRACE
  init_disasm("riscv64-pc-linux-gnu");
#endif

#ifdef CONFIG_VCD
  Verilated::traceEverOn(true);
  vcd_ptr = new VerilatedVcdC;
  top->trace(vcd_ptr, 5);
  vcd_ptr->open("waveform.vcd");
#endif
  init_npc();
  int sim_period = 20;
  while (cpu.state && sim_period--)
  {
    exec_once();
  }
  exit_npc();
  return 0;
}