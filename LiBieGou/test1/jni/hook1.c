#include <sys/ptrace.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>
#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>
#include <sys/syscall.h>

long getSysCallNo(int pid, struct pt_regs *regs)
{
    long scno = 0;
    scno = ptrace(PTRACE_PEEKTEXT, pid, (void *)(regs->ARM_pc - 4), NULL);
    if(scno == 0)
        return 0;
         
    if (scno == 0xef000000) {
        scno = regs->ARM_r7;
    } else {
        if ((scno & 0x0ff00000) != 0x0f900000) {
            return -1;
        }
        scno &= 0x000fffff;
    }
    return scno;    
}

void hookSysCallBefore(pid_t pid)
{
    struct pt_regs regs;
    int sysCallNo = 0;
    
    ptrace(PTRACE_GETREGS, pid, NULL, &regs);    
    sysCallNo = getSysCallNo(pid, &regs);
    printf("Before SysCallNo = %d\n",sysCallNo);
    
    if(sysCallNo == __NR_write)
    {
        printf("__NR_write: %ld %p %ld\n",regs.ARM_r0,(void*)regs.ARM_r1,regs.ARM_r2);
    }
}

void hookSysCallAfter(pid_t pid)
{
    struct pt_regs regs;
    int sysCallNo = 0;

    ptrace(PTRACE_GETREGS, pid, NULL, &regs);  
    sysCallNo = getSysCallNo(pid, &regs);
    
    printf("After SysCallNo = %d\n",sysCallNo);
    
    if(sysCallNo == __NR_write)
    {
        printf("__NR_write return: %ld\n",regs.ARM_r0);
    }
    
    printf("\n");
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
        
    ptrace(PTRACE_SYSCALL, pid, NULL, NULL);
    
    while(1)
    {
        wait(&status);
        hookSysCallBefore(pid);
        ptrace(PTRACE_SYSCALL, pid, NULL, NULL);
        
        wait(&status);
        hookSysCallAfter(pid);
        ptrace(PTRACE_SYSCALL, pid, NULL, NULL);
    }
    
    ptrace(PTRACE_DETACH, pid, NULL, NULL);
    return 0;
}
