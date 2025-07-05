package com.github.son_daehyeon.global.infra.mysql;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@NoArgsConstructor
public abstract class BaseSchema {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    @NotNull
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    UUID id;

    @Column(updatable = false, nullable = false)
    @NotNull
    @CreatedDate
    LocalDateTime createdAt;

    @Column(nullable = false)
    @NotNull
    @LastModifiedDate
    LocalDateTime updatedAt;

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (Objects.isNull(obj) || !getClass().equals(obj.getClass())) {
            return false;
        }

        BaseSchema that = (BaseSchema) obj;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {

        return id.hashCode();
    }
}
