LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := sqrt-demo
LOCAL_SRC_FILES := sqrt-demo.c

include $(BUILD_SHARED_LIBRARY)
