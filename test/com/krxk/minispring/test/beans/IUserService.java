package com.krxk.minispring.test.beans;

public interface IUserService {
    void queryUserInfo();
    String register(String userName);
    String getToken();
}
