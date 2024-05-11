package com.magazine.client.controller;

import com.magazine.client.model.TestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.client.RestClient;

import java.security.Principal;

@Controller
@Slf4j
public class TestController {

    private final RestClient client;
    private final OAuth2AuthorizedClientManager clientManager;

    public TestController(ClientRegistrationRepository repository, OAuth2AuthorizedClientRepository authorizedClientRepository){
        this.clientManager = new DefaultOAuth2AuthorizedClientManager(repository, authorizedClientRepository);
        this.client = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .requestInterceptor((request, body, execution) -> {
                    log.info("DETAILS :" + SecurityContextHolder.getContext().getAuthentication().getDetails());
                    if(SecurityContextHolder.getContext().getAuthentication().getName().equals("test-user")){
                        log.info("CONTEXT HOLDER INFO " + SecurityContextHolder.getContext().getAuthentication());
                        log.info("NAME: " + SecurityContextHolder.getContext().getAuthentication().getName());
                        var token = this.clientManager.authorize(OAuth2AuthorizeRequest
                                        .withClientRegistrationId("client-app-authorization-code")
                                        .principal(SecurityContextHolder.getContext().getAuthentication())
                                        .build())
                                .getAccessToken()
                                .getTokenValue();
                        request.getHeaders().setBearerAuth(token);
                    }
                    return execution.execute(request, body);
                }).build();
    }

    @GetMapping("/test/access")
    public String testRequest(Model model){
        model.addAttribute("testResponse",
                this.client.get()
                        .uri("/kernel/test/access")
                        .retrieve()
                        .body(TestResponse.class));
        return "test";
    }

    @GetMapping("/test/read")
    public String testRead(Model model){
        model.addAttribute("testResponse",
                this.client.get()
                        .uri("/kernel/test/read")
                        .retrieve()
                        .body(TestResponse.class));
        return "test";
    }

    @ModelAttribute("principal")
    public Principal principal(Principal principal){
        return principal;
    }
}
