package org.repository.impl;

import org.entity.Token;
import org.repository.TokenRepository;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class TokenRepositoryImpl implements TokenRepository {
    // token table in DB is tokenName as key and Token as value
    private Map<String, Token> tokenTable;

    public TokenRepositoryImpl() {
        tokenTable = new ConcurrentHashMap();
    }

    @Override
    public void addToken(Token token) {
        tokenTable.put(token.getTokenName(), token);
    }

    @Override
    public Boolean deleteToken(String tokenName) {
        if(tokenExists(tokenName)) {
            tokenTable.remove(tokenName);
            return true;
        }
        return false;
    }

    @Override
    public Token getToken(String tokenName) {
        return tokenTable.get(tokenName);
    }

    @Override
    public Boolean tokenExists(String tokenName) {
        if(Objects.isNull(tokenName)) return false;
        return tokenTable.containsKey(tokenName);
    }
}
