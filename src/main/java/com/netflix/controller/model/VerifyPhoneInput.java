package com.netflix.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VerifyPhoneInput {
    private String otp;
}
