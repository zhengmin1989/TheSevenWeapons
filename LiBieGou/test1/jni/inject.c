#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <android/log.h>
#include <elf.h>
#include <fcntl.h>

#define LOG_TAG "DEBUG"
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##args)

int mzhengHook(char * str){
    printf("mzheng Hook pid = %d\n", getpid());
    printf("Hello %s\n", str);
    LOGD("mzheng Hook pid = %d\n", getpid());
    LOGD("Hello %s\n", str);
    return 0;
}

