package org.repository.impl;

import org.repository.TokenUserRepository;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class TokenUserRepositoryImpl implements TokenUserRepository {

    // tokenUser table in DB is tokenName as key and userName as value
    // 1-to-1 mapping
    private Map<String, String> tokenUserRelationMap;
    private Map<String, String> userTokenRelationMap;

    public TokenUserRepositoryImpl() {
        tokenUserRelationMap = new ConcurrentHashMap();
        userTokenRelationMap = new ConcurrentHashMap<>();
    }

    @Override
    public void addRelation(String tokenName, String userName) {
        tokenUserRelationMap.put(tokenName, userName);
        userTokenRelationMap.put(userName, tokenName);
    }

    @Override
    public Boolean deleteRelationByToken(String tokenName) {
        if(relationExistsByToken(tokenName)) {
            String userName = tokenUserRelationMap.get(tokenName);
            userTokenRelationMap.remove(userName);
            tokenUserRelationMap.remove(tokenName);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteRelationByUser(String userName) {
        if(relationExistsByUser(userName)) {
            String tokenName = userTokenRelationMap.get(userName);
            tokenUserRelationMap.remove(tokenName);
            userTokenRelationMap.remove(userName);
            return true;
        }
        return false;
    }

    @Override
    public String getUserByToken(String tokenName) {
        return tokenUserRelationMap.get(tokenName);
    }

    @Override
    public String getTokenByUser(String userName) {
        return userTokenRelationMap.get(userName);
    }

    @Override
    public Boolean relationExistsByToken(String tokenName) {
        if(Objects.isNull(tokenName)) return false;
        return tokenUserRelationMap.containsKey(tokenName);
    }

    @Override
    public Boolean relationExistsByUser(String userName) {
        if(Objects.isNull(userName)) return false;
        return userTokenRelationMap.containsKey(userName);
    }
}
