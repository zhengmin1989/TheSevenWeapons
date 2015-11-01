LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := target
LOCAL_SRC_FILES := target.c.arm

include $(BUILD_EXECUTABLE)

###########################################################

include $(CLEAR_VARS)
LOCAL_MODULE    := hook1
LOCAL_SRC_FILES := hook1.c

include $(BUILD_EXECUTABLE)

###########################################################

include $(CLEAR_VARS)
LOCAL_MODULE    := hook2
LOCAL_SRC_FILES := hook2.c

include $(BUILD_EXECUTABLE)

###########################################################

include $(CLEAR_VARS)
LOCAL_MODULE    := hook3
LOCAL_SRC_FILES := hook3.c

include $(BUILD_EXECUTABLE)


###########################################################

include $(CLEAR_VARS)
LOCAL_MODULE    := inject
LOCAL_SRC_FILES := inject.c
LOCAL_LDLIBS := -llog 

include $(BUILD_SHARED_LIBRARY)

###########################################################

include $(CLEAR_VARS)
LOCAL_MODULE    := inject2
LOCAL_SRC_FILES := inject2.c.arm
LOCAL_LDLIBS := -llog 

include $(BUILD_SHARED_LIBRARY)

###########################################################

include $(CLEAR_VARS)
LOCAL_MODULE    := hook4
LOCAL_SRC_FILES := hook4.c

include $(BUILD_EXECUTABLE)

###########################################################

include $(CLEAR_VARS)
LOCAL_MODULE    := hook5
LOCAL_SRC_FILES := hook5.c

include $(BUILD_EXECUTABLE)




