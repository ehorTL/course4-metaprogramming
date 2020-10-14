package com.yehorpolishchuk.lexercpp.token;

public  class Token {
    private TokenName name;
    private TokenValue value;

    private TokenMetadata meta;

    public Token(TokenNameAllowed tokenName, String value){
        this.name = new TokenName(tokenName);
        this.value = new TokenValue(value);
        this.meta = null;
    }

    public void setName(TokenNameAllowed tokenName) {
        this.name = new TokenName(tokenName);
    }

    public TokenName getName() {
        return name;
    }

    public TokenValue getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value.getValue();
    }

    public void setMeta (TokenMetadata meta) {
        this.meta = meta;
    }

    public TokenMetadata getMeta(){
        return this.meta;
    }
}
