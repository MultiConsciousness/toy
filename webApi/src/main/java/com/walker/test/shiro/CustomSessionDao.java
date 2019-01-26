package com.walker.test.shiro;

import com.walker.test.common.SysConstant;
import com.walker.test.configuration.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomSessionDao extends AbstractSessionDAO {

    @Autowired
    private JedisUtil jedisUtil;
    @Autowired
    private RedisSerializer serializer;
    @Autowired
    private StringSerializer stringSerializer;
    @Autowired
    private ObjectSerializer objectSerializer;

    private int sessionExpire = SysConstant.ShiroConfig.DEFAULT_TTL;

    private byte[] formatKey(String key) {
        return serializer.stringSerialize(stringSerializer,SysConstant.ShiroConfig.SHIRO_SESSION_PREFIX + key);
    }

    private void saveSession(Session session) {
        if (null != session && null != session.getId()) {
            byte[] key = formatKey(session.getId().toString());
            byte[] value = serializer.objectSerialize(objectSerializer,session);
            jedisUtil.setWithExpire(key, value, sessionExpire);
        }
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (null == sessionId)
            return null;
        byte[] key = formatKey(sessionId.toString());
        byte[] value = jedisUtil.get(key);
        if (null != value && value.length != 0)
            return (Session) serializer.objectDeserialize(objectSerializer,value);
        return null;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if(null == session || null == session.getId())
            return ;
        byte[] key = formatKey(session.getId().toString());
        jedisUtil.delete(key);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        byte[] keyPattern = formatKey("*");
        Set<Session> sessions = new HashSet<>();
        Set<byte[]> keySet = jedisUtil.keys(keyPattern);
        if(CollectionUtils.isEmpty(keySet))
            return sessions;
        for(byte[] key: keySet){
            byte[] value = jedisUtil.get(key);
            if(null != value && value.length != 0){
                Session session = (Session)serializer.objectDeserialize(objectSerializer,value);
                sessions.add(session);
            }
        }
        return sessions;
    }

    public int getSessionExpire() {
        return sessionExpire;
    }

    public void setSessionExpire(int sessionExpire) {
        this.sessionExpire = sessionExpire;
    }
}
