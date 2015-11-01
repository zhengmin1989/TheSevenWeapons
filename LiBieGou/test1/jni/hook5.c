#include <stdio.h>
#include <stdlib.h>
#include <asm/user.h>
#include <asm/ptrace.h>
#include <sys/ptrace.h>
#include <sys/wait.h>
#include <sys/mman.h>
#include <dlfcn.h>
#include <dirent.h>
#include <unistd.h>
#include <string.h>
#include <elf.h>
#include <android/log.h>

#define CPSR_T_MASK     ( 1u << 5 )

const char *libc_path = "/system/lib/libc.so";

const int long_size = sizeof(long);

int ptrace_setregs(pid_t pid, struct pt_regs * regs)
{
    if (ptrace(PTRACE_SETREGS, pid, NULL, regs) < 0) {
        perror("ptrace_setregs: Can not set register values");
        return -1;
    }

    return 0;
}

int ptrace_continue(pid_t pid)
{
    if (ptrace(PTRACE_CONT, pid, NULL, 0) < 0) {
        perror("ptrace_cont");
        return -1;
    }

    return 0;
}

void putdata(pid_t child, long addr,
             char *str, int len)
{   char *laddr;
    int i, j;
    union u {
            long val;
            char chars[long_size];
    }data;
    i = 0;
    j = len / long_size;
    laddr = str;
    while(i < j) {
        memcpy(data.chars, laddr, long_size);
        ptrace(PTRACE_POKEDATA, child,
               addr + i * 4, data.val);
        ++i;
        laddr += long_size;
    }
    j = len % long_size;
    if(j != 0) {
        memcpy(data.chars, laddr, j);
        ptrace(PTRACE_POKEDATA, child,
               addr + i * 4, data.val);
    }
}


void* get_module_base(pid_t pid, const char* module_name)
{
    FILE *fp;
    long addr = 0;
    char *pch;
    char filename[32];
    char line[1024];

    if (pid == 0) {
        snprintf(filename, sizeof(filename), "/proc/self/maps");
    } else {
        snprintf(filename, sizeof(filename), "/proc/%d/maps", pid);
    }

    fp = fopen(filename, "r");

    if (fp != NULL) {
        while (fgets(line, sizeof(line), fp)) {
            if (strstr(line, module_name)) {
                pch = strtok( line, "-" );
                addr = strtoul( pch, NULL, 16 );

                if (addr == 0x8000)
                    addr = 0;

                break;
            }
        }

        fclose(fp) ;
    }

    return (void *)addr;
}


long get_remote_addr(pid_t target_pid, const char* module_name, void* local_addr)
{
    void* local_handle, *remote_handle;

    local_handle = get_module_base(0, module_name);
    remote_handle = get_module_base(target_pid, module_name);

    long ret_addr = (long)((uint32_t)local_addr + (uint32_t)remote_handle - (uint32_t)local_handle);

    return ret_addr;
}


int ptrace_call(pid_t pid, uint32_t addr, long *params, uint32_t num_params, struct pt_regs* regs)
{
    uint32_t i;
    for (i = 0; i < num_params && i < 4; i ++) {
        regs->uregs[i] = params[i];
    }

    //
    // push remained params onto stack
    //
    if (i < num_params) {
        regs->ARM_sp -= (num_params - i) * sizeof(long) ;
        putdata(pid, regs->ARM_sp, (char*)&params[i], (num_params - i) * sizeof(long));
    }

    regs->ARM_pc = addr;
    if (regs->ARM_pc & 1) {
        /* thumb */
        regs->ARM_pc &= (~1u);
        regs->ARM_cpsr |= CPSR_T_MASK;
    } else {
        /* arm */
        regs->ARM_cpsr &= ~CPSR_T_MASK;
    }

    regs->ARM_lr = 0;
    
    if (ptrace_setregs(pid, regs) == -1
            || ptrace_continue(pid) == -1) {
        printf("error\n");
        return -1;
    }

    int stat = 0;
    waitpid(pid, &stat, WUNTRACED);
    while (stat != 0xb7f) {
        if (ptrace_continue(pid) == -1) {
            printf("error\n");
            return -1;
        }
        waitpid(pid, &stat, WUNTRACED);
    }

    return 0;
}


void injectSo(pid_t pid,char* so_path, char* function_name,char* parameter)
{
    struct pt_regs old_regs,regs;
    long mmap_addr, dlopen_addr, dlsym_addr, dlclose_addr;

//save old regs

    ptrace(PTRACE_GETREGS, pid, NULL, &old_regs);
    memcpy(&regs, &old_regs, sizeof(regs));

//get remote addres

    printf("getting remote addres:\n");
    mmap_addr = get_remote_addr(pid, libc_path, (void *)mmap);
    dlopen_addr = get_remote_addr( pid, libc_path, (void *)dlopen );
    dlsym_addr = get_remote_addr( pid, libc_path, (void *)dlsym );
    dlclose_addr = get_remote_addr( pid, libc_path, (void *)dlclose );
    
    printf("mmap_addr=%p dlopen_addr=%p dlsym_addr=%p dlclose_addr=%p\n",
    (void*)mmap_addr,(void*)dlopen_addr,(void*)dlsym_addr,(void*)dlclose_addr);
    
    
    long parameters[10];

//mmap

    parameters[0] = 0; //address
    parameters[1] = 0x4000; //size
    parameters[2] = PROT_READ | PROT_WRITE | PROT_EXEC; //WRX
    parameters[3] = MAP_ANONYMOUS | MAP_PRIVATE; //flag
    parameters[4] = 0; //fd
    parameters[5] = 0; //offset
    
    ptrace_call(pid, mmap_addr, parameters, 6, &regs);
    ptrace(PTRACE_GETREGS, pid, NULL, &regs);

    long map_base = regs.ARM_r0;
    printf("map_base = %p\n", (void*)map_base);

//dlopen

    printf("save so_path = %s to map_base = %p\n", so_path, (void*)map_base);
    putdata(pid, map_base, so_path, strlen(so_path) + 1);

    parameters[0] = map_base;
    parameters[1] = RTLD_NOW| RTLD_GLOBAL;

    ptrace_call(pid, dlopen_addr, parameters, 2, &regs);
    ptrace(PTRACE_GETREGS, pid, NULL, &regs);
    
    long handle = regs.ARM_r0;
    
    printf("handle = %p\n",(void*) handle);

//dlsym

    printf("save function_name = %s to map_base = %p\n", function_name, (void*)map_base);
    putdata(pid, map_base, function_name, strlen(function_name) + 1);

    parameters[0] = handle;
    parameters[1] = map_base;

    ptrace_call(pid, dlsym_addr, parameters, 2, &regs);
    ptrace(PTRACE_GETREGS, pid, NULL, &regs);
    
    long function_ptr = regs.ARM_r0;

    printf("function_ptr = %p\n", (void*)function_ptr);

//function_call

    printf("save parameter = %s to map_base = %p\n", parameter, (void*)map_base);
    putdata(pid, map_base, parameter, strlen(parameter) + 1);

    parameters[0] = map_base;

    ptrace_call(pid, function_ptr, parameters, 1, &regs);

//restore old regs

    ptrace(PTRACE_SETREGS, pid, NULL, &old_regs);
}


int main(int argc, char *argv[])
{
    if(argc != 2) {
        printf("Usage: %s <pid to be traced>\n", argv[0]);
        return 1;
    }
                                                                                                     
    pid_t pid;
    int status;
    pid = atoi(argv[1]);
    
    if(0 != ptrace(PTRACE_ATTACH, pid, NULL, NULL))
    {
        printf("Trace process failed:%d.\n", errno);
        return 1;
    }
    
    char* so_path = "/data/local/tmp/libinject2.so";
    char* function_name = "mzhengHook";
    char* parameter = "sevenWeapons";
    injectSo(pid, so_path, function_name, parameter);
    
    ptrace(PTRACE_DETACH, pid, NULL, 0);
    
    return 0;
}
