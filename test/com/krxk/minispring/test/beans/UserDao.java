package com.krxk.minispring.test.beans;

import java.util.HashMap;

public class UserDao {
    private static HashMap<String, String> hashMap = new HashMap<>();

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }
    public void initDataMethod() {
        System.out.println("执行 UserDao.init-method");
        hashMap.put("1", "Krxk-1");
        hashMap.put("2", "Krxk-2");
        hashMap.put("3", "Krxk-3");
    }
    public void destroyDataMethod() {
        System.out.println("执行 UserDao.destroy-method");
        hashMap.clear();
    }
}
