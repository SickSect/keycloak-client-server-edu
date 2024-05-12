package com.magazine.kernel.config;

import com.magazine.kernel.utils.CustomJwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ResourceServerConfig {

    private final CustomJwtConverter converter;

    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/test/read").hasRole("READ")
                        .requestMatchers("/test/write").hasRole("WRITE")
                        .anyRequest().authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .oauth2ResourceServer(oauth -> oauth.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(converter)
                ))


                /*(oauth -> oauth
                        .jwt(jwtConfigurer -> {
                            JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
                            converter.setPrincipalClaimName("preferred_username");
                            jwtConfigurer.jwtAuthenticationConverter(converter);

                            JwtGrantedAuthoritiesConverter customGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
                            customGrantedAuthoritiesConverter.setAuthorityPrefix("");
                            customGrantedAuthoritiesConverter.setAuthoritiesClaimName("groups");
                            JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

                            converter.setJwtGrantedAuthoritiesConverter(token ->
                                Stream.concat(grantedAuthoritiesConverter.convert(token).stream(),
                                        customGrantedAuthoritiesConverter.convert(token).stream()).toList());
                        }))*/
                .build();
    }
}
