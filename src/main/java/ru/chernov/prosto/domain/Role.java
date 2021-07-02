package ru.chernov.prosto.domain;

import org.springframework.security.core.GrantedAuthority;


/**
 * @author Pavel Chernov
 */
public enum Role implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
