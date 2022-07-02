package com.abidevel.oauth.healthmetricservice.config;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter{

    private final KeyConfiguration keyConfiguration;

    public WebSecurity (KeyConfiguration keyConfiguration) {
        this.keyConfiguration = keyConfiguration;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO Auto-generated method stub
        http.oauth2ResourceServer((t) -> t.jwt(c -> {
            c.decoder(jwtDecoder());
            c.jwtAuthenticationConverter(jwtAuthenticationToken());
        }));
            
        http.authorizeRequests()
            .mvcMatchers(HttpMethod.DELETE, "/profile/**").hasAuthority("admin")
            .mvcMatchers(HttpMethod.DELETE, "/metric/**").hasAuthority("admin")
            .mvcMatchers(HttpMethod.POST, "/advice/**").hasAuthority("advice")
            .anyRequest().authenticated();
            
    }
    

    @Bean
    public JwtDecoder jwtDecoder() {
        try {
            KeyFactory keyStore = KeyFactory.getInstance("RSA");
            byte[] bytes = Base64.getDecoder().decode(keyConfiguration.getPublicKey()); 
            X509EncodedKeySpec x509 = new X509EncodedKeySpec(bytes);
            RSAPublicKey pubKey = (RSAPublicKey) keyStore.generatePublic(x509);
            return NimbusJwtDecoder.withPublicKey(pubKey).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Invalid Public Key");
        }
    }

    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationToken () {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter((auth) ->  {
            List<String> authorities = (List<String>) auth.getClaims().get("authorities");
            return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        });
        return converter;
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension () {
        return new SecurityEvaluationContextExtension();
    }
}