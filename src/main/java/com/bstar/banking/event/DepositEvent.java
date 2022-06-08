package com.bstar.banking.event;

import org.springframework.context.ApplicationEvent;

public class DepositEvent extends ApplicationEvent {

    public DepositEvent(Object source) {
        super(source);
    }
}
