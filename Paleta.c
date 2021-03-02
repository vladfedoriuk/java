
#include <stdio.h>
#include <string.h>
#include "Paleta.h"

JNIEXPORT void JNICALL Java_Paleta_printText
(JNIEnv *env, jobject obj, jobject c)
{
    // obj == this
    // c == Color()
    jclass cls = (*env)->GetObjectClass(env, c);
    jmethodID mid = (*env)->GetStaticMethodID(env, cls, "values", "()[LColor;");
    jobjectArray joa = (*env)->CallStaticObjectMethod(env, c, mid);

    int len = (*env)->GetArrayLength(env, joa);

    jfieldID fidRed = (*env)->GetFieldID(env, cls, "r", "D");
    jfieldID fidGreen = (*env)->GetFieldID(env, cls, "g", "D");
    jfieldID fidBlue = (*env)->GetFieldID(env, cls, "b", "D");

    jdouble currentRed = (*env)->GetDoubleField(env, c, fidRed);
    jdouble currentGreen = (*env)->GetDoubleField(env, c, fidGreen);
    jdouble currentBlue = (*env)->GetDoubleField(env, c, fidBlue);

    jmethodID midName = (*env)->GetMethodID(env, cls,"name", "()Ljava/lang/String;");

    jstring current_jstr = (*env)->CallObjectMethod(env, c, midName);
    const char* current_str = (*env)->GetStringUTFChars(env, current_jstr, NULL);

    printf("\t %s %g %g %g\t\n", current_str, currentRed, currentGreen, currentBlue);

    printf("Enum type 'Color' contains %d elements\n", len);
    int i =0;
    for(i=0; i<len; i++) {
        jobject elem = (*env)->GetObjectArrayElement(env, joa, i);


        jdouble red  = (*env)->GetDoubleField(env, elem, fidRed);
        jdouble green = (*env)->GetDoubleField(env, elem, fidGreen);
        jdouble blue = (*env)->GetDoubleField(env, elem, fidBlue);

        jstring jstr = (*env)->CallObjectMethod(env, elem, midName);
        const char* str = (*env)->GetStringUTFChars(env, jstr, NULL);

        if(strcmp(current_str,str) == 0) {
            printf("    ===> %s %g %g %g <===\n", str, red, green, blue);
        } else {
            printf("\t %s %g %g %g\t\n", str, red, green, blue);
        }

        (*env)->ReleaseStringUTFChars(env, jstr, str);
    }

    (*env)->ReleaseStringUTFChars(env, current_jstr, current_str);
}