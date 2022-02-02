package com.sopromadze.blogapi.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class ResponseBodyMatchers {

    @Getter
    @Setter
    @NoArgsConstructor
    static public class ErrorResult {
        private String error;
        private int status;
        private List<String> messages;
        private String timestamp;
    }
}
