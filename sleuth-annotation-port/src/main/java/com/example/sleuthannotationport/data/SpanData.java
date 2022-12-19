package com.example.sleuthannotationport.data;

import io.micrometer.common.docs.KeyName;

public enum SpanData {
    ;

    public enum Tags implements KeyName {
        CLASS {
            @Override
            public String asString() {
                return "class";
            }
        },
        METHOD {
            @Override
            public String asString() {
                return "method";
            }
        }
    }
}
