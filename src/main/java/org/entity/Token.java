package org.entity;

import java.time.OffsetDateTime;

public class Token {
    private String tokenName;

    private String userName;
    private OffsetDateTime expiredTime;

    public Token(String tokenName, String userName, OffsetDateTime expiredTime) {
        this.tokenName = tokenName;
        this.userName = userName;
        this.expiredTime = expiredTime;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public OffsetDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(OffsetDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }
}
