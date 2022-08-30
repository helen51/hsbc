package org.entity;

import java.time.OffsetDateTime;

public class Token {
    private String tokenName;

    private OffsetDateTime expiredTime;

    public Token(String tokenName, OffsetDateTime expiredTime) {
        this.tokenName = tokenName;
        this.expiredTime = expiredTime;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public OffsetDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(OffsetDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }
}
