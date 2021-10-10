package com.commerce.web.trade.dto;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MemberDetail {

    private String iss;
    private Long exp;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("client_id")
    private String clientId;
    private String[] authorities;
    private String[] scope;
    
    public String getIss() {
        return iss;
    }
    public void setIss(String iss) {
        this.iss = iss;
    }
    public Long getExp() {
        return exp;
    }
    public void setExp(Long exp) {
        this.exp = exp;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public String[] getAuthorities() {
        return authorities;
    }
    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }
    public String[] getScope() {
        return scope;
    }
    public void setScope(String[] scope) {
        this.scope = scope;
    }
    
    @Override
    public String toString() {
        return "MemberDetail [iss=" + iss + ", exp=" + exp + ", userName=" + userName + ", clientId=" + clientId
                + ", authorities=" + Arrays.toString(authorities) + ", scope=" + Arrays.toString(scope) + "]";
    }
    
    
}
