package com.github.son_daehyeon.global.security.authentication;

import com.github.son_daehyeon.domain.user.schema.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@RequiredArgsConstructor
public class UserAuthentication implements Authentication {

    private final User user;
    private final String password;

    @Getter
    @Setter
    private boolean authenticated = false;

    public UserAuthentication(User user) {

        this(user, null);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return user.getRole().getAuthorization();
    }

    @Override
    public String getCredentials() {

        return password;
    }

    @Override
    public Object getDetails() {

        return null;
    }

    @Override
    public User getPrincipal() {

        return user;
    }

    @Override
    public String getName() {

        return user.getId().toString();
    }
}