package com.purdue.a407.cryptodisco.Security;

public interface HmacSigner {
    public String sign(String message, String secret);
}
