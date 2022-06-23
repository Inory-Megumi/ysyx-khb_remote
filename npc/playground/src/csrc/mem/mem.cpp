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
  long long raddr = Raddr;
  Raddr &= ~0x7ull;//bus align
  (*Rdata) = *((long long *)guest_to_host(Raddr));
  printf("read:  addr:%016llx content:%016llx\n",raddr,(*Rdata));
  return;
}
void pmem_write(long long Waddr, long long Wdata, char Wmask)
{
  if (!in_memory(Waddr))
    return;
  Waddr &= ~0x7ull;//bus align
  for (int i = 0; i < 7; i++)
  {
    uint8_t *Vaddr = guest_to_host(Waddr);
    if ((Wmask >> i) & 1)
      *((uint8_t *)(Vaddr + i)) = ((Wdata) >> (i * 8)) & (0xFF);
  }
  printf("write: addr:%016llx Wmask:%08d content:%016llx\n",Waddr,Wmask,Wdata);
  
  return;
}
extern "C" void inst_parse(long long pc,int inst){//interpret inst
    cpu.inst = inst;//for inst parse
    cpu.pc = pc;
}
