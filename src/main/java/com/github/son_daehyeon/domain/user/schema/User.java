package com.github.son_daehyeon.domain.user.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.son_daehyeon.global.infra.mysql.BaseSchema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseSchema {

    @Column(nullable = false, unique = true)
    @NotNull
    String email;

    @Column(nullable = false)
    @NotNull
    @JsonIgnore
    String password;

    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    Role role;

    public enum Role {

        MEMBER,
        ADMIN(MEMBER),
        ;

        private final Role[] inheritedRoles;

        Role(Role... inheritedRoles) {

            this.inheritedRoles = inheritedRoles;
        }

        public Collection<SimpleGrantedAuthority> getAuthorization() {

            Set<Role> authorization = new HashSet<>();

            collectAuthorization(this, authorization);

            return authorization.stream().map(role -> "ROLE_" + role.name()).map(SimpleGrantedAuthority::new).toList();
        }

        private void collectAuthorization(Role role, Set<Role> roles) {

            roles.add(role);

            for (Role inheritedRole : role.inheritedRoles) {

                collectAuthorization(inheritedRole, roles);
            }
        }
    }
}

