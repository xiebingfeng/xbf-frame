package com.krt.frame.ext

import java.lang.reflect.Method

fun methodInvoke(anyObject: Any, method: Method, args: Array<Any>?): Any? {
    var result: Any? = null
    if (null != args && args.isNotEmpty()) {
        when (args.size) {
            1 -> result = method.invoke(anyObject, args[0])
            2 -> result = method.invoke(anyObject, args[0], args[1])
            3 -> result = method.invoke(anyObject, args[0], args[1], args[2])
            4 -> result = method.invoke(anyObject, args[0], args[1], args[2], args[3])
            5 -> result = method.invoke(anyObject, args[0], args[1], args[2], args[3], args[4])
            6 -> result = method.invoke(anyObject, args[0], args[1], args[2], args[3], args[4], args[5])
            7 -> result = method.invoke(anyObject, args[0], args[1], args[2], args[3], args[4], args[5], args[6])
            8 -> result =
                    method.invoke(anyObject, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])
            9 -> result =
                    method.invoke(
                            anyObject, args[0], args[1], args[2], //
                            args[3], args[4], args[5], args[6], args[7], args[8]
                    )
            10 -> result =
                    method.invoke(
                            anyObject, args[0], args[1], args[2], //
                            args[3], args[4], args[5], args[6], args[7], args[8], args[9]
                    )

        }
    } else {
        result = method.invoke(anyObject)
    }
    return result
}