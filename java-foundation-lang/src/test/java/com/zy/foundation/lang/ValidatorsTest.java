package com.zy.foundation.lang;

import com.zy.foundation.lang.exception.ErrorInfo;
import com.zy.foundation.lang.validator.Validators;
import org.junit.Test;

public class ValidatorsTest {
    @Test
    public void fn01() {
        Validators.ifBlank("").thenThrow(new ErrorInfo() {
            @Override
            public String getCode() {
                return "500";
            }

            @Override
            public String getMsg() {
                return "参数错误";
            }
        });
    }
}
