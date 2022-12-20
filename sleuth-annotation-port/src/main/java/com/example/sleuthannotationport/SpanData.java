package com.example.sleuthannotationport;

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

    public enum Events {
        BEFORE("%s.before"),
        AFTER("%s.after");

        private final String value;

        Events(final String value) {
            this.value = value;
        }
    }

}
