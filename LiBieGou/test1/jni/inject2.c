#include <android/log.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <fcntl.h>
#include <sys/ptrace.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <dlfcn.h>
#include <elf.h>
#include <unistd.h>
#include <errno.h>       
#include <sys/mman.h>
#include <termios.h>
#include <sys/ioctl.h>

#define LOG_TAG "DEBUG"
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##args)


long hookaddr = 0x84f4;
char* targetPath = "/data/local/tmp/target";
static struct hook_t eph;

struct hook_t {
	unsigned int jump[3];
	unsigned int store[3];
	unsigned int orig;
	unsigned int patch;
};

void inline hook_cacheflush(unsigned int begin, unsigned int end)
{	
	const int syscall = 0xf0002;

	__asm __volatile (
		"mov	 r0, %0\n"			
		"mov	 r1, %1\n"
		"mov	 r7, %2\n"
		"mov     r2, #0x0\n"
		"svc     0x00000000\n"
		:
		:	"r" (begin), "r" (end), "r" (syscall)
		:	"r0", "r1", "r7"
		);
}

void hook_precall(struct hook_t *h)
{
	int i;
    for (i = 0; i < 3; i++)
    	((int*)h->orig)[i] = h->store[i];
    	
	hook_cacheflush((unsigned int)h->orig, (unsigned int)h->orig+sizeof(h->jump)*10);
}

void hook_postcall(struct hook_t *h)
{
	int i;
	
	for (i = 0; i < 3; i++)
		((int*)h->orig)[i] = h->jump[i];

	hook_cacheflush((unsigned int)h->orig, (unsigned int)h->orig+sizeof(h->jump)*10);	
}


int hook_direct(struct hook_t *h, unsigned int addr, void *hookf)
{
	int i;
	
	printf("addr  = %x\n", addr);
	printf("hookf = %x\n", (unsigned int)hookf);

//mprotect
    mprotect((void*)0x8000, 0xa000-0x8000, PROT_READ|PROT_WRITE|PROT_EXEC);

//modify function entry	
	h->patch = (unsigned int)hookf;
	h->orig = addr;
	h->jump[0] = 0xe59ff000; // LDR pc, [pc, #0]
	h->jump[1] = h->patch;
	h->jump[2] = h->patch;
	for (i = 0; i < 3; i++)
		h->store[i] = ((int*)h->orig)[i];
	for (i = 0; i < 3; i++)
		((int*)h->orig)[i] = h->jump[i];

//cacheflush	
	hook_cacheflush((unsigned int)h->orig, (unsigned int)h->orig+sizeof(h->jump));
	return 1;
}

void  __attribute__ ((noinline)) my_sevenWeapons(int number)
{
    printf("sevenWeapons() called, number = %d\n", number);
    number*=2;

	void (*orig_sevenWeapons)(int number);
	orig_sevenWeapons = (void*)eph.orig;
    
	hook_precall(&eph);
	orig_sevenWeapons(number);
	hook_postcall(&eph);
	
}


int mzhengHook(char * str){
    printf("mzheng Hook pid = %d\n", getpid());
    printf("Hello %s\n", str);
    hook_direct(&eph,hookaddr,my_sevenWeapons);
    
    return 0;
}

