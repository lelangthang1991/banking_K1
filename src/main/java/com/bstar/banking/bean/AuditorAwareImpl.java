package com.bstar.banking.bean;

import com.bstar.banking.exception.NotFoundException;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static com.bstar.banking.common.UserString.EMAIL_NOT_FOUND;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotFoundException(EMAIL_NOT_FOUND);
        }
        return Optional.ofNullable(authentication.getName());
    }
}
