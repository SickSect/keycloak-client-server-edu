package com.magazine.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestResponse {
    private String test;
    private String errorCode;
    private String reason;
}
