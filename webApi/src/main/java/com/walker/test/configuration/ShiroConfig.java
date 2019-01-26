package com.walker.test.configuration;

import com.walker.test.common.SysConstant;
import com.walker.test.shiro.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        LinkedHashMap<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put(SysConstant.ShiroConfig.MARK_KEY,kickoutSessionControlFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/templates/**", "anon");
        filterChainDefinitionMap.put("/view/index","anon");
        filterChainDefinitionMap.put("/user/login", "anon");
        filterChainDefinitionMap.put("/user/logout", "anon");
        filterChainDefinitionMap.put("/**","user,kickOut");
        //后端接口的url并非页面
        shiroFilterFactoryBean.setLoginUrl("/view/index");
        shiroFilterFactoryBean.setSuccessUrl("/view/home");
        shiroFilterFactoryBean.setUnauthorizedUrl("/view/unauthorized");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        /*
         *还可以对默认的登录界面、登陆成功的界面、
         * 未授权的想到界面、自定义的过滤器链进行设置
         * */
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm());
        securityManager.setSessionManager(sessionManager());
        securityManager.setCacheManager(redisCacheManager());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    @Bean
    public CustomRealm customRealm() {
        CustomRealm customRealm = new CustomRealm();
        //开启缓存
        customRealm.setCachingEnabled(true);
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return customRealm;
    }

    /*
    * 解决： 无权限页面不跳转 shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized") 无效
     * shiro的源代码ShiroFilterFactoryBean.Java定义的filter必须满足filter instanceof AuthorizationFilter，
     * 只有perms，roles，ssl，rest，port才是属于AuthorizationFilter，而anon，authcBasic，auchc，user是AuthenticationFilter，
     * 所以unauthorizedUrl设置后页面不跳转 Shiro注解模式下，登录失败与没有权限都是通过抛出异常。
     * 并且默认并没有去处理或者捕获这些异常。在SpringMVC下需要配置捕获相应异常来通知用户信息
    * */
    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver(){
        SimpleMappingExceptionResolver simpleMappingExceptionResolver = new SimpleMappingExceptionResolver();
        Properties properties = new Properties();
        properties.setProperty("org.apache.shiro.authz.UnauthorizedException","unauthorized");
        properties.setProperty("org.apache.shiro.authz.UnauthenticatedException","index");
        simpleMappingExceptionResolver.setExceptionMappings(properties);
        return simpleMappingExceptionResolver;
    }

    /*
    * 解决springboot whitelabel error page,显示特定的错误界面
    * */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> serverFactoryCustomizer(){
        return factory -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED,"/view/unauthorized");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND,"/view/404");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,"/view/500");
            factory.addErrorPages(error401Page,error404Page,error500Page);
        };
    }

    /*
    *  cookie对象;会话Cookie模板 ,默认为: JSESSIONID 问题: 与SERVLET容器名冲突,重新定义为sid或rememberMe
    * */
    @Bean
    public SimpleCookie rememberMeCookie(){
        //cookie的名称，和前端页面rememberMe的checkbox的name属性相对
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //setCookie的httpOnly属性为true之后，会增加cookie的安全性
        //只能通过http访问cookie，javascript无法访，防止xxs读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //cookie保存30天
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    /*
    * cookie管理对象,rememberMe管理器
    * */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdnf"));
        return cookieRememberMeManager;
    }

    /*
    * FormAuthenticationFilter过滤器 过滤记住我
    * */
    @Bean
    public FormAuthenticationFilter formAuthenticationFilter(){
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        // 与前端checkbox中的name属性相对
        formAuthenticationFilter.setRememberMeParam("rememberMe");
        return formAuthenticationFilter;
    }

    /*
    * spring静态注入,有些属性在容器启动之后就不会再变化，也只要注入一次就行了
    * 在控制并发登录时有用,单点登录踢出旧的登录信息
    * */
    @Bean
    public MethodInvokingFactoryBean getMethodInvokingFactoryBean(){
        MethodInvokingFactoryBean factoryBean = new MethodInvokingFactoryBean();
        factoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        factoryBean.setArguments(securityManager());
        return factoryBean;
    }

    /*
    * 监听shiro session
    * */
    @Bean("shiroSessionListener")
    public ShiroSessionListener shiroSessionListener(){
        return new ShiroSessionListener();
    }

    /*
    * 配置Session Id生成器
    * */
    @Bean
    public SessionIdGenerator sessionIdGenerator(){
        return new JavaUuidSessionIdGenerator();
    }

   /* @Bean
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("127.0.0.1:6379");
        //redis连接超时时间5s
        redisManager.setTimeout(5000);
        return redisManager;
    }*/

    /*
    * 操作session在redis中的Dao
    * */
    /*@Bean
    public SessionDAO sessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        //session在redis中保存的时间最好大于session会话超时的时间
        redisSessionDAO.setExpire(12000);
        return redisSessionDAO;
    }*/

    /*@Bean
    public RedisCacheManager redisCacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //缓存时间3分钟
        redisCacheManager.setExpire(180000);
        redisCacheManager.setPrincipalIdFieldName("account");
        return redisCacheManager;
    }*/

    /*
    * 上面是rememberMe的cookie，这里是session的cookie，由于与Servlet的默认模板冲突，需要重新命名、配置
    * */
    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        //-1表示浏览器关闭时此Cookie失效
        simpleCookie.setMaxAge(-1);
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        return simpleCookie;
    }

    /*
    * 配置SessionManager
    * */
    @Bean
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<>();
        listeners.add(shiroSessionListener());
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionDAO(customSessionDao());
        sessionManager.setCacheManager(redisCacheManager());
        //设置全局会话超时时间,1h
        sessionManager.setGlobalSessionTimeout(1800000);
        //是否开启删除无效的session，默认true
        sessionManager.setDeleteInvalidSessions(true);
        //是否开启调度器定时检测
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //设置Session失效的扫描时间，清理用户直接关闭浏览器造成的孤立session，默认62分钟
        //设置了该属性就不需要设置ExecutorServiceSessionValidationScheduler
        //因为底层也是调用ExecutorServiceSessionValidationScheduler来执行检测
        sessionManager.setSessionValidationInterval(3600000);
        //取消url后面的JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }


    //============================================================================================================================================
    @Bean
    public RedisCacheManager redisCacheManager(){
        return new RedisCacheManager();
    }

    @Bean
    public CustomSessionDao customSessionDao(){
        return new CustomSessionDao();
    }
    //============================================================================================================================================

    @Bean
    public KickOutSessionControlFilter kickoutSessionControlFilter(){
        KickOutSessionControlFilter kickoutSessionControlFilter = new KickOutSessionControlFilter();
        kickoutSessionControlFilter.setCache(redisCacheManager());
        kickoutSessionControlFilter.setKickOutAfter(false);
        kickoutSessionControlFilter.setSessionManager(sessionManager());
        kickoutSessionControlFilter.setMaxSession(1);
        kickoutSessionControlFilter.setKickOutUrl("/view/index");
        return kickoutSessionControlFilter;
    }

    /*
     * 下面两个@Bean配置开启shiro在ioc容器中的注解功能，借助springaop实现对shiro注解行为的解读
     * */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    /*
    * 开启shiro注解模式
    * 如@RequiresPermissions("userInfo:add")
    * */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    /*用于ioc容器中shiro bean 生命周期的管理(初始化/销毁)*/
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


}
