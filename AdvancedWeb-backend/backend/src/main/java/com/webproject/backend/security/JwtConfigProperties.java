package com.webproject.backend.security;


import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ConfigurationProperties(prefix = "jwt.token")
public class JwtConfigProperties {

    @NonNull private int validity;
    @NonNull private String secret;
}
