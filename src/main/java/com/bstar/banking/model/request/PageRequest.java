package com.bstar.banking.model.request;

import lombok.Data;

@Data
public class PageRequest {
    private String keyword;
    private String sort;
    private String page;
    private String size;
}
