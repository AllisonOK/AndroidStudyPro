#include "com_example_ndk_JNITest.h"

JNIEXPORT jstring JNICALL
Java_com_example_ndk_JNITest_get(JNIEnv *env, jobject instance)
{

    return (*env)->NewStringUTF(env, "i am from native c.");

}