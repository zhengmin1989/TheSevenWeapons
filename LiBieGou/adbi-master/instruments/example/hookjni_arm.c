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

#include <sys/types.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <jni.h>

extern jstring my_Java_com_mzheng_libiegou_test2_MainActivity_stringFromJNI(JNIEnv* env,jobject thiz,jint a,jint b);

jstring my_Java_com_mzheng_libiegou_test2_MainActivity_stringFromJNI_arm(JNIEnv* env,jobject thiz,jint a,jint b)
{
	return my_Java_com_mzheng_libiegou_test2_MainActivity_stringFromJNI(env, thiz, a, b);
}
