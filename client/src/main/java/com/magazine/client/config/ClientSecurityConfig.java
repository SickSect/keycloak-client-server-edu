package com.magazine.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.stream.Stream;

@Configuration
public class ClientSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .oauth2Client(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults())
                .build();
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserOAuth2UserService(){
        OidcUserService oidcUserService = new OidcUserService();
        return userRequest -> {
            OidcUser user = oidcUserService.loadUser(userRequest);
            List<GrantedAuthority> grantedAuthorityList = Stream.concat(user.getAuthorities().stream(),
                    user.getClaimAsStringList("groups").stream()
                            .filter(auth -> auth.startsWith("ROLE_"))
                            .map(SimpleGrantedAuthority::new))
                    .toList();
            return new DefaultOidcUser(grantedAuthorityList,
                    user.getIdToken(),
                    user.getUserInfo(),
                    "preferred_username");
        };
    }
}
