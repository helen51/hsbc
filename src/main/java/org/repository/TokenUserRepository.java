package org.repository;

import org.entity.Token;

public interface TokenUserRepository {
    void addRelation(String tokenName, String userName);
    Boolean deleteRelationByToken(String tokenName);
    Boolean deleteRelationByUser(String userName);
    String getUserByToken(String tokenName);
    String getTokenByUser(String userName);
    Boolean relationExistsByToken(String tokenName);
    Boolean relationExistsByUser(String userName);
}
