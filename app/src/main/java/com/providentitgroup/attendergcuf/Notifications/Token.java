package com.providentitgroup.attendergcuf.Notifications;

public class Token {
    private  String token;
    public static  String mtoken;

    public Token(String token) {
        this.token = token;
        Token.mtoken=token;
    }

    public Token() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
