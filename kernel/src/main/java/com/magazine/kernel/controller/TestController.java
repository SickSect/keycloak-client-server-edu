package com.magazine.kernel.controller;

import com.magazine.kernel.model.TestResponse;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
public class TestController {
    
    @GetMapping("/access")
    public TestResponse getAccess(Principal principal){
        TestResponse response = new TestResponse();
        response.setTest("This is access test. user - " + principal.getName());
        response.setReason("200 SUCCESS");
        response.setErrorCode("0");
        return response;
    }

    @GetMapping("/read")
    public TestResponse getRead(Principal principal){
        TestResponse response = new TestResponse();
        response.setTest("This is READ test. user - " + principal.getName());
        response.setReason("200 SUCCESS");
        response.setErrorCode("0");
        return response;
    }

    @GetMapping("/write")
    public TestResponse getWrite(Principal principal){
        TestResponse response = new TestResponse();
        response.setTest("This is WRITE test. user - " + principal.getName());
        response.setReason("200 SUCCESS");
        response.setErrorCode("0");
        return response;
    }
}
