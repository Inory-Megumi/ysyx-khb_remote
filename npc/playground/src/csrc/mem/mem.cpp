#include "npc.h"

uint8_t pmem[CONFIG_MSIZE] = {0};

uint8_t *guest_to_host(long long paddr) { return pmem + paddr - CONFIG_MBASE; }
long long host_to_guest(uint8_t *haddr) { return haddr - pmem + CONFIG_MBASE; }

bool in_memory(long long addr)
{
    if (addr < CONFIG_MBASE || addr >= CONFIG_MSIZE + CONFIG_MBASE)
        return false;
    return true;
}

void pmem_read(long long Raddr, long long *Rdata)
{
  if (!in_memory(Raddr))
  {*Rdata = 0;
    return;}
  Raddr &= ~0x7ull;//bus align
  (*Rdata) = *((long long *)guest_to_host(Raddr));
  return;
}
extern "C" void inst_parse(long long pc,int inst){//interpret inst
    cpu.inst = inst;//for inst parse
    cpu.pc = pc;
}
