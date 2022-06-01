package com.bstar.banking.utils;

import org.springframework.stereotype.Component;

@Component
public class DeviceType {
    public String parseXForwardedHeader(String header) {
        return header.split(" *, *")[0];
    }

}
