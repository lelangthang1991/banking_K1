package com.bstar.banking.model.response;


public class RestResponse<T> extends CommonResponse<T> {

    public RestResponse(T body) {
        super();
        this.setBody(body);
    }

    public RestResponse(HeaderResponse header, T body) {
        super(header, body);
    }

}
