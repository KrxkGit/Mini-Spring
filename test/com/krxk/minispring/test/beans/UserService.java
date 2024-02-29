package com.krxk.minispring.test.beans;

import com.krxk.minispring.beans.factory.DisposableBean;
import com.krxk.minispring.beans.factory.InitializingBean;

public class UserService implements InitializingBean, DisposableBean {
    private String uid;
    private String company;
    private UserDao userDao;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void queryUserInfo() {
        System.out.println("查询用户信息 " + uid + " 公司:" +company + " " + userDao.queryUserName(uid));
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("执行：UserService.destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("执行：UserService.afterPropertiesSet");
    }
}
