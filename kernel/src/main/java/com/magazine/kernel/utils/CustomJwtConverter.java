package com.magazine.kernel.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    // TODO move it to cfg
    private final String principalAttribute = "preferred_username";
    private final String resourceId = "client-web-app";

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        Collection<GrantedAuthority> authorities = Stream.concat(jwtGrantedAuthoritiesConverter.convert(source).stream(),
                extractRoles(source).stream()
        ).collect(Collectors.toSet());
        return new JwtAuthenticationToken(source,
                authorities,
                getPrincipalClaimName(source));
    }

    private String getPrincipalClaimName(Jwt source) {
        String claimName = JwtClaimNames.SUB;
        if (principalAttribute != null)
            claimName = principalAttribute;
        return source.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractRoles(Jwt source) {
        Map<String, Object> resourceAccess;
        Map<String, Object> resource;
        Collection<String> resourceRoles;
        if (source.getClaim("resource_access") == null){
            return Set.of();
        }
        resourceAccess = source.getClaim("resource_access");
        if (resourceAccess.get("client-web-app") == null){
            return Set.of();
        }
        resource = (Map<String, Object>) resourceAccess.get("client-web-app");
        resourceRoles = (Collection<String>) resource.get("roles");
        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}
