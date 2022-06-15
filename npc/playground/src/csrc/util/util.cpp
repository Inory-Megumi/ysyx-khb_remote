#include "npc.h"
#define ebreak 0x00100073
extern "C" void set_gpr_ptr(const svOpenArrayHandle r)
{
    uint64_t *cpu_gpr = NULL;
    cpu_gpr = (uint64_t *)(((VerilatedDpiOpenVar *)r)->datap());
    for (int i = 0; i < 32; i++)
        cpu.gpr[i] = cpu_gpr[i];
}



void check_state()
{  
    if(cpu.inst == ebreak)
        cpu.state = false;
    return;
}

long ld(char *img_file)
{
    if (img_file == NULL)
    {
        printf("No image is given.\n");
        exit(1);
    }

    FILE *fp = fopen(img_file, "rb");
    assert(fp);

    fseek(fp, 0, SEEK_END);
    long size = ftell(fp);

    printf("The image is %s, size = %ld \n", img_file, size);

    fseek(fp, 0, SEEK_SET);
    int ret = fread(pmem, size, 1, fp);
    assert(ret == 1);

    fclose(fp);

    return size;
}