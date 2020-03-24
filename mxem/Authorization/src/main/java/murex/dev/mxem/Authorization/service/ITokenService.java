package murex.dev.mxem.Authorization.service;

import murex.dev.mxem.Authorization.exception.TokenNotValidException;
import murex.dev.mxem.Authorization.model.Token;

import java.util.Optional;


public interface ITokenService {
    void save(Token token);

    Token findById(Long id);

    Token findByUsername(String username);

    void update(Token token);

    void delete(String id);

    Boolean tokenExists(String token);

    String getUserFromToken(String token) throws TokenNotValidException;

}
