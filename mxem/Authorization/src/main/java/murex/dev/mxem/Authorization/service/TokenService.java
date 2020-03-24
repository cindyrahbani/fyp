package murex.dev.mxem.Authorization.service;

import lombok.extern.slf4j.Slf4j;
import murex.dev.mxem.Authorization.exception.TokenNotValidException;
import murex.dev.mxem.Authorization.model.Token;
import murex.dev.mxem.Authorization.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class TokenService implements  ITokenService{

    @Autowired
    TokenRepository tokenRepository;

    @Override
    public void save(Token token){
        List<Token> tok = tokenRepository.findByUsername(token.getUsername());
        if(tok.size()==0){
            tokenRepository.save(token);}
        else{
            Token newTok= new Token();
            newTok.setId(tok.get(0).getId());
            newTok.setToken(token.getToken());
            newTok.setUsername(token.getUsername());
            tokenRepository.save(newTok);
        }


    }

    @Override
    public Token findById(Long id) {
        return tokenRepository.findById(id).get();
    }

    @Override
    public Token findByUsername(String username) {
            List<Token> tok = tokenRepository.findByUsername(username);
            if(tok.size()==0){
                return null;
            }
            return tokenRepository.findByUsername(username).get(0);
    }



    @Override
    public void update(Token token){
        tokenRepository.save(token);
    }

    @Override
    public void delete(String id){
        tokenRepository.delete(findByUsername(id));
    }

    @Override
    public Boolean tokenExists(String token){
        List<Token> tokens = tokenRepository.findByToken(token);
        if(tokens.size()==0){
            return false;
        }
        return true;
    }

    @Override
    public String getUserFromToken(String token) throws TokenNotValidException {
        List<Token> tokens = tokenRepository.findByToken(token);
        if(tokens.size()==0){
            throw new TokenNotValidException();
        }
        Token tok = tokens.get(0);
        return tok.getUsername();

    }
}
