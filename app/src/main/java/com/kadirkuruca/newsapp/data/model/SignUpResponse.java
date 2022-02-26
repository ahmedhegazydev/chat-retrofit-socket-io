package com.kadirkuruca.newsapp.data.model;

import com.nurbk.ps.demochat.model.ApiUser;

public class SignUpResponse {
    private ApiUser apiUser;
    private String token;

    public SignUpResponse(ApiUser socketUser, String token) {
        this.apiUser = socketUser;
        this.token = token;
    }

    public ApiUser getApiUser() {
        return apiUser;
    }

    public void setApiUser(ApiUser apiUser) {
        this.apiUser = apiUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
