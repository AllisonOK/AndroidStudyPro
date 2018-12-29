package com.example.ndk;

/**
 * Created by allison on 2018/12/19.
 */

public class JNITest {

    static {
        System.loadLibrary("JNITest");
    }

    public native static String get();

}
