package com.task.newsapp.data.model;

import com.nurbk.ps.demochat.model.ApiUser;

public class SignInResponse {
    private ApiUser apiUser;
    private String token;

    public SignInResponse(ApiUser apiUser, String token) {
        this.apiUser = apiUser;
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
