package com.zy.foundation.lang.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Trace {
    // 这里也可以加一些属性, 比如只输出入参日志, 只输出回参日志, 或序列化等
}
