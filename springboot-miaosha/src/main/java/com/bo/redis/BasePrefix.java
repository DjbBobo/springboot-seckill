package com.bo.redis;

public abstract class BasePrefix implements KeyPrefix{
    private String prefix;

    public BasePrefix(String prefix){
        this.prefix = prefix;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }
}
