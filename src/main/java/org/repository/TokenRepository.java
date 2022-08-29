package org.repository;

import org.entity.Token;

public interface TokenRepository {
    void addToken(Token token);
    Boolean deleteToken(String tokenName);
    Token getToken(String tokenName);
    Boolean tokenExists(String tokenName);
}
