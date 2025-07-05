package com.github.son_daehyeon.domain.auth.schema;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Data
@Builder
@RedisHash(value = "refresh_token")
public class RefreshToken {

    @Id
    Long id;

    @Indexed
    String token;

    UUID userId;

    @TimeToLive(unit = TimeUnit.HOURS)
    long ttl;
}