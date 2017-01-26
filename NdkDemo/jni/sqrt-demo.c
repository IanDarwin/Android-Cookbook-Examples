#include <stdio.h>
#include <stdlib.h>

#include "foo_ndkdemo_SqrtDemo.h"

JNIEXPORT jdouble JNICALL Java_foo_ndkdemo_SqrtDemo_sqrtC(
	JNIEnv *env, jclass clazz, jdouble d) {
	
	jdouble x0 = 10.0, x1 = d, diff;
	do {
		x1 = x0 - (((x0 * x0) - d) / (x0 * 2));
		diff = x1 - x0;
		// printf("x0=%12.8f, x1=%12.8f, diff=%12.8f\n",
		// x0, x1, diff);
		x0 = x1;
	} while (labs(diff) > foo_ndkdemo_SqrtDemo_EPSILON);
	return x1;
}
