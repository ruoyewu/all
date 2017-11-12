//
// Created by 张群 on 2017/11/6.
//

#include <jni.h>
#include <ctime>
#include <string>
#include <sstream>
using namespace std;

extern "C"
JNIEXPORT jstring
JNICALL
java_com_wuruoye_all2_base_util_NetUtil_getSecretKey(
        JNIEnv *env,
        jobject /* this */) {
    long now = time(0);
    int day = localtime(&now)->tm_mday;
    long time_secret = now * day;
    stringstream ss;
    ss << time_secret;
    string str = ss.str();

    string secret;
    for (int i = 0; i < str.size(); ++i) {

    }

    return env->NewStringUTF(secret.c_str());
}