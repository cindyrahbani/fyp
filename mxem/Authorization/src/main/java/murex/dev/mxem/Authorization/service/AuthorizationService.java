package murex.dev.mxem.Authorization.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import murex.dev.mxem.Authorization.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class AuthorizationService {

    @Autowired
    DiscoveryClient discoveryClient;

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public AuthorizationService(SecretKey secretKey,
                                JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    public String getUsernameFromToken(String token){
        token = token.replace(jwtConfig.getTokenPrefix(), "");

        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();
            String username = (String) body.get("sub");
            return username;
        } catch (JwtException e) {
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
        }
    }

}