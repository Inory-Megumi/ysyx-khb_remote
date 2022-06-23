#include "npc.h"
#include "util.h"

#ifdef CONFIG_DIFFTEST
void (*ref_difftest_memcpy)(paddr_t addr, void *buf, size_t n, bool direction) = NULL;
void (*ref_difftest_regcpy)(void *dut, bool direction) = NULL;
void (*ref_difftest_exec)(uint64_t n) = NULL;
void (*ref_difftest_raise_intr)(uint64_t NO) = NULL;
void (*ref_difftest_init)() = NULL;

void init_so(char *ref_so_file, long img_size)
{
  assert(ref_so_file != NULL);

  void *handle;
  handle = dlopen(ref_so_file, RTLD_LAZY);
  assert(handle);

  ref_difftest_memcpy = dlsym(handle, "difftest_memcpy");
  assert(ref_difftest_memcpy);

  ref_difftest_regcpy = dlsym(handle, "difftest_regcpy");
  assert(ref_difftest_regcpy);

  ref_difftest_exec = dlsym(handle, "difftest_exec");
  assert(ref_difftest_exec);

  ref_difftest_raise_intr = dlsym(handle, "difftest_raise_intr");
  assert(ref_difftest_raise_intr);

  void (*ref_difftest_init)() = dlsym(handle, "difftest_init");
  assert(ref_difftest_init);

  printf("The result of every instruction will be compared with %s. "
         "This will help you a lot for debugging, but also significantly reduce the performance. "
         "If it is not necessary, you can turn it off in menuconfig.\n",
         ref_so_file);

  ref_difftest_init();
  ref_difftest_memcpy(RESET_VECTOR, pmem, img_size, DIFFTEST_TO_REF);
  ref_difftest_regcpy(&cpu, DIFFTEST_TO_REF);
}

bool check_regs_npc(CPU_state ref_cpu)
{
  bool result = true;
  for (int i = 0; i < 32; i++)
  {
    if (cpu.gpr[i] != ref_cpu.gpr[i])
    {
      printf("" RED "Missing match reg%d, npc_val=%lx, nemu_val=%lx" NONE "\n", i, cpu.gpr[i], ref_cpu.gpr[i]);
      result = false;
    }
  }
  if (cpu.pc != ref_cpu.pc)
  {
    printf("" RED "Missing match at pc, npc_val=%lx,nemu_val=%lx" NONE "\n", cpu.pc, ref_cpu.pc);
    result = false;
  }
  return result;
}
#endif