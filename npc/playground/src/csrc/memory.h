#include <stdio.h>
#include <assert.h>
#include <string.h>
#include "verilated.h" //Defines common routines
#include "Vkcore.h"
typedef unsigned long addr_t;
typedef unsigned int inst_t;
typedef unsigned char uint8_t;
#define ARRLEN(arr) (int)(sizeof(arr) / sizeof(arr[0]))
#define RESET_VECTOR 0x00000000080000000
#define ebreak 0x00100073
#define depth 100000//memory depth
// for ebreak
extern bool stop;
bool stop = false;


static long *pmem = new long[depth];
//static char const *img_file = "/home/khb/project/ysyx-workbench/am-kernels/tests/cpu-tests/build/dummy-riscv64-nemu.bin";


long* guest_to_host(addr_t paddr) { return pmem + (paddr - RESET_VECTOR)/8; }

static inline long host_read(void *addr) {
  return *(long *)addr;//return 64 bits
}
/*static inline void host_write(void *addr, int len, word_t data) {
  switch (len) {
    case 1: *(uint8_t  *)addr = data; return;
    case 2: *(uint16_t *)addr = data; return;
    case 4: *(uint32_t *)addr = data; return;
    IFDEF(CONFIG_ISA64, case 8: *(uint64_t *)addr = data; return);
    IFDEF(CONFIG_RT_CHECK, default: assert(0));
  }
}*/
static const long img[] = {//test instruction 
  0x0010009300000093,//ra<-$o+0
  0x0030009300200093,
  0x0010007300408093,
//ebreak
};
/*
static inst_t pmem_read(addr_t pc){
  inst_t inst = host_read(guest_to_host(pc));
  return inst;
}
static void pmem_write(paddr_t addr, int len, word_t data) {
  host_write(guest_to_host(addr), len, data);
}*/


extern "C" void pmem_read(long raddr, long *rdata) {
   long addr = raddr & ~0x7ull;
   //printf("addr:%lx\n",addr);
   if(addr != 0)
   *rdata = host_read(guest_to_host(addr));
   else 
   *rdata = 0;
}
/*
extern "C" void pmem_write(long long waddr, long long wdata, char wmask) {
  // 总是往地址为`waddr & ~0x7ull`的8字节按写掩码`wmask`写入`wdata`
  // `wmask`中每比特表示`wdata`中1个字节的掩码,
  // 如`wmask = 0x3`代表只写入最低2个字节, 内存中的其它字节保持不变
}*/
extern "C" void sim_stop(int inst){
    if(inst == 0x00100073) stop = true;
}


void init_isa() {
  /* Load built-in image. */
  memcpy(guest_to_host(RESET_VECTOR), img, sizeof(img));
}

void  load_img(const char *img_file) {
  if (img_file == NULL) {
    printf("No image is given. Use the default build-in image.\n");
    return; // built-in image size
  }
  FILE *fp = fopen(img_file, "rb");
  //Assert(fp, "Can not open '%s'", img_file);
  fseek(fp, 0, 2);
  long size = ftell(fp);
  //Log("The image is %s, size = %ld", img_file, size);
  fseek(fp, 0, 0);
  int ret = fread(guest_to_host(RESET_VECTOR), size, 1, fp);
  assert(ret == 1);
  fclose(fp);
  return;
}