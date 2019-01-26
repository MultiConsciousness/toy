package com.walker.test.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.util.concurrent.atomic.AtomicInteger;

public class ShiroSessionListener implements SessionListener {

    private final AtomicInteger sessionCount = new AtomicInteger(0);

    /*
    * 新建会话时人数+1
    * */
    @Override
    public void onStart(Session session) {
        sessionCount.incrementAndGet();
    }

    /*
    * 退出会话时触发
    * */
    @Override
    public void onStop(Session session) {
        sessionCount.decrementAndGet();
    }

    /*
    * session过期时调用
    * */
    @Override
    public void onExpiration(Session session) {
        sessionCount.decrementAndGet();
    }

    /*
    * 获取在线人数
    * */
    public AtomicInteger getActiveUser(){
        return sessionCount;
    }
}
