package murex.dev.mxem.Authorization.controller;

import com.google.common.base.Strings;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import murex.dev.mxem.Authorization.config.UsernameAndPasswordAuthenticationRequest;
import murex.dev.mxem.Authorization.exception.TokenNotValidException;
import murex.dev.mxem.Authorization.model.RolesPermissions;
import murex.dev.mxem.Authorization.model.Token;
import murex.dev.mxem.Authorization.config.JwtConfig;
import murex.dev.mxem.Authorization.service.TokenService;
import murex.dev.mxem.Authorization.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@Validated
@RefreshScope
public class ValidateController {

    @Autowired
    UserService userService;

    String ldapUrl = "ldap://root-dc.murex.com:3268";
    String ldapDomain = "murex.com";
    ActiveDirectoryLdapAuthenticationProvider defaultLdapProvider = new ActiveDirectoryLdapAuthenticationProvider(ldapDomain, ldapUrl);

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public ValidateController(SecretKey secretKey,
                            JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Autowired
    TokenService tokenService;

    @PostMapping("/authorize")
    public ResponseEntity<String> login(@RequestBody UsernameAndPasswordAuthenticationRequest user, HttpServletResponse response){
        String username= user.getUsername();
        String password= user.getPassword();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                password
        );
        Authentication auth =  defaultLdapProvider.authenticate(authentication);

        ArrayList<String> authority = new ArrayList<>();
        RolesPermissions rolesPermissions= userService.getRolesPermissions(username);

        String token = Jwts.builder()
                .setSubject(auth.getName())
//                .claim("authorities", ["ADMIN"])
                .claim("roles", rolesPermissions.getRoles())
                .claim("permissions",rolesPermissions.getPermissions())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();
        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
        response.addHeader("Access-Control-Expose-Headers","Authorization");
        Token tok = new Token(auth.getName(),token);
        tokenService.save(tok);

        return(ResponseEntity.ok("Authentication Successful"));
    }


    @PostMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String token){
        if (Strings.isNullOrEmpty(token) || !token.startsWith("Bearer ")) {
            return new ResponseEntity<Void>( HttpStatus.UNAUTHORIZED );
        }
        token = token.replace("Bearer ", "");
        try {

       Boolean res = tokenService.tokenExists(token);

            if(res==true) {
                log.info("Token is valid");
                return new ResponseEntity<Void>( HttpStatus.OK );}
            else{
                log.info("Token is not valid");
            return new ResponseEntity<Void>( HttpStatus.UNAUTHORIZED);
        }



    } catch (JwtException e) {
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
        }


    }

    @PostMapping("/identify")
    public ResponseEntity<Void> identifyUser(@RequestHeader("Authorization") String token, HttpServletResponse response){
        if (Strings.isNullOrEmpty(token) || !token.startsWith("Bearer ")) {
            return new ResponseEntity<Void>( HttpStatus.UNAUTHORIZED );
        }
        token = token.replace("Bearer ", "");
        try {

            String user = tokenService.getUserFromToken(token);

            if(user!=null) {
                log.info("Token is valid");
                response.addHeader("Username",user);
                return new ResponseEntity<Void>( HttpStatus.OK );}
            else{
                log.info("Token is not valid");
                return new ResponseEntity<Void>( HttpStatus.UNAUTHORIZED);
            }



        } catch (JwtException e) {
            throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
        } catch (TokenNotValidException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
