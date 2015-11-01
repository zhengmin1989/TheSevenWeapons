/*
 *  Collin's Binary Instrumentation Tool/Framework for Android
 *  Collin Mulliner <collin[at]mulliner.org>
 *  http://www.mulliner.org/android/
 *
 *  (c) 2012,2013
 *
 *  License: LGPL v2.1
 *
 */

#define _GNU_SOURCE
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <dlfcn.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <sys/select.h>
#include <string.h>
#include <termios.h>
#include <pthread.h>
#include <sys/epoll.h>
#include <stdlib.h>
#include <jni.h>

#include <jni.h>
#include <stdlib.h>

#include "../base/hook.h"
#include "../base/base.h"

#undef log

#define log(...) \
        {FILE *fp = fopen("/data/local/tmp/adbi_example.log", "a+"); if (fp) {\
        fprintf(fp, __VA_ARGS__);\
        fclose(fp);}}


// this file is going to be compiled into a thumb mode binary

void __attribute__ ((constructor)) my_init(void);

static struct hook_t eph;

// for demo code only
static int counter;

// arm version of hook
extern jstring my_Java_com_mzheng_libiegou_test2_MainActivity_stringFromJNI_arm(JNIEnv* env,jobject thiz,jint a,jint b);

/*  
 *  log function to pass to the hooking library to implement central loggin
 *
 *  see: set_logfunction() in base.h
 */
static void my_log(char *msg)
{
	log("%s", msg)
}

jstring my_Java_com_mzheng_libiegou_test2_MainActivity_stringFromJNI(JNIEnv* env,jobject thiz,jint a,jint b)
{
	jstring (*orig_stringFromJNI)(JNIEnv* env,jobject thiz,jint a,jint b);
	orig_stringFromJNI = (void*)eph.orig;
	
	a = 10;
	b = 10;

	hook_precall(&eph);
	jstring res = orig_stringFromJNI(env, thiz, a, b);
	if (counter) {
		hook_postcall(&eph);
		log("stringFromJNI() called\n");
		counter--;
		if (!counter)
			log("removing hook for stringFromJNI()\n");
	}
    
	return res;
}

void my_init(void)
{
	counter = 3;

	log("%s started\n", __FILE__)
 
	set_logfunction(my_log);

	hook(&eph, getpid(), "libhello-jni.", "Java_com_mzheng_libiegou_test2_MainActivity_stringFromJNI", my_Java_com_mzheng_libiegou_test2_MainActivity_stringFromJNI_arm, my_Java_com_mzheng_libiegou_test2_MainActivity_stringFromJNI);
}

