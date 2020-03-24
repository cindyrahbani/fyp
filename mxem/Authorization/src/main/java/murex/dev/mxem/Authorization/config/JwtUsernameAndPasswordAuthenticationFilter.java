package murex.dev.mxem.Authorization.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.SneakyThrows;
import murex.dev.mxem.Authorization.model.Token;
import murex.dev.mxem.Authorization.repository.TokenRepository;
import murex.dev.mxem.Authorization.service.TokenService;
import murex.dev.mxem.Authorization.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;


/**
 * This class is used to verify credentials
 */

public class JwtUsernameAndPasswordAuthenticationFilter  extends UsernamePasswordAuthenticationFilter {


    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private ActiveDirectoryLdapAuthenticationProvider defaultLdapProvider;
    private String ldapUrl;
    private String ldapDomain;


    TokenService tokenService= BeanUtil.getBean(TokenService.class);

    @Autowired
    public JwtUsernameAndPasswordAuthenticationFilter(
                                                      JwtConfig jwtConfig,
                                                      SecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
        this.ldapUrl = "ldap://root-dc.murex.com:3268";
        this.ldapDomain = "murex.com";
        this.defaultLdapProvider = new ActiveDirectoryLdapAuthenticationProvider(ldapDomain, ldapUrl);

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new    ObjectMapper()
                    .readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()
            );
            return defaultLdapProvider.authenticate(authentication);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @SneakyThrows
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        throw new Exception("Login Failed");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        ArrayList<String> authority = new ArrayList<>();
        authority.add("ADMIN");
        authority.add("USER");
        String token = Jwts.builder()
                .setSubject(authResult.getName())
//                .claim("authorities", ["ADMIN"])
                .claim("authorities", authority)
                .claim("mxem", "testing")
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();
        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
        Token tok = new Token(authResult.getName(),token);
        Token res = tokenService.findByUsername(authResult.getName());
        if(res==null)
        {
            tokenService.save(tok);
        }
        else{
            tokenService.update(tok);
        }

    }
}
