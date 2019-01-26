package com.walker.test.shiro;

import com.walker.test.common.SysConstant;
import com.walker.test.dao.entity.User;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

public class KickOutSessionControlFilter extends AccessControlFilter {

    private static Logger log = LoggerFactory.getLogger(KickOutSessionControlFilter.class);

    /*踢出后跳转的地址*/
    private String kickOutUrl;
    /*踢出先登录的用户，还是当前登录用户，默认时之前登录的用户*/
    private boolean kickOutAfter = false;
    /*同一个账号最大的会话数:1 */
    private int maxSession = 1;
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    /*
    * 表示拒绝访问时，是否自己处理；返回true则表示自己不处理而且拦截器链继续执行，返回false则表示自己处理完毕（如重定向到别的url）
    * */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest, servletResponse);
        if(!subject.isAuthenticated() && !subject.isRemembered()){
            //表示未登录状态，继续执行拦截器链
            return true;
        }
        Session session = subject.getSession();
        /*
        *此处获取到的principal是account,是因为在自定义的realm的认证方法中,在new SimpleAuthenticationInfo()的构造函数中
        * 传入的是account而不是User对象，所以获取到的是String类型的account
        * */
        User user = (User)subject.getPrincipal();
        Serializable sessionId = session.getId();
        /*初始化用户队列放到缓存里*/
        Deque<Serializable> deque = cache.get(user.getAccount());
        if(null == deque){
            deque = new LinkedList<>();
            cache.put(user.getAccount(),deque);
        }
        /*如果队列中没有这个sessionId，而且用户没有被踢出,则放入队列中*/
        if(!deque.contains(sessionId) && session.getAttribute(SysConstant.ShiroConfig.MARK_KEY) == null){
            deque.push(sessionId);
            cache.put(user.getAccount(),deque);
        }
        Session kickedOutSession;
        while(deque.size()>maxSession){
            Serializable kickedOutSessionId;
            if(kickOutAfter){//若表示踢出当前用户
               // kickedOutSessionId = deque.getFirst();
                kickedOutSessionId = deque.removeFirst();
            }else{//表示踢出之前登录的人
                kickedOutSessionId = deque.removeLast();
            }
            try{
                kickedOutSession = sessionManager.getSession(new DefaultSessionKey(kickedOutSessionId));
                if(null != kickedOutSession)
                    //设置会话kickOut属性为true，表示踢出
                    kickedOutSession.setAttribute(SysConstant.ShiroConfig.MARK_KEY,SysConstant.ShiroConfig.MARK_VALUE);
            }catch(Exception e){
                log.error(SysConstant.RedisMessage.MARK_SESSION_EXCEPTION,e);
            }
        }
        /*如果被设置为踢出,直接强制logout，重定向到登陆界面*/
        if(null != session.getAttribute(SysConstant.ShiroConfig.MARK_KEY)){
            try{
                //强制logout
                subject.logout();
                System.out.println("---------------------------踢出重复登录用户成功-----------------------");
            }catch(Exception e){
                log.error(SysConstant.RedisMessage.FORCE_LOGOUT_FAILED);
            }
            WebUtils.issueRedirect(servletRequest,servletResponse, kickOutUrl);
            return false;
        }
        return true;
    }

    public String getKickOutUrl() {
        return kickOutUrl;
    }

    public void setKickOutUrl(String kickOutUrl) {
        this.kickOutUrl = kickOutUrl;
    }

    public boolean isKickOutAfter() {
        return kickOutAfter;
    }

    public void setKickOutAfter(boolean kickOutAfter) {
        this.kickOutAfter = kickOutAfter;
    }

    public int getMaxSession() {
        return maxSession;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public Cache<String, Deque<Serializable>> getCache() {
        return cache;
    }

    public void setCache(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("");
    }
}
