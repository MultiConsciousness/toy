package com.walker.test.common;

public class SysConstant {

    public interface AuthenAndAuthor{
        String ACCOUNT_NOT_EXIST = "账号不存在";
        String ERROR_ACCOUNT_OR_PWD ="账号或者密码不正确";
        int LOCK_STATUS = 1;
        String LOCKED_MSG = "账号已被锁定";
    }

    public interface IllegalArgument{
        int ILLEGAL_ID = 0;
    }

    public interface ShiroConfig{
        int DEFAULT_TTL = 1800;
        String SHIRO_SESSION_PREFIX = "shiro-session:";
        String SHIRO_CACHE_PREFIX = "shiro-cache:";
        String CURRENT_USER = "activeUser";
        String CURRENT_USERID = "activeUserId";
        int DEFAULT_COUNT = 100;
        String MARK_KEY = "kickOut";
        String MARK_VALUE = "true";
    }
    public interface RedisMessage {
        String QUERY_ERROR = "从连接池获取连接失败或者从缓存读取失败";
        String SET_ERROR = "从连接池获取连接失败或者设置值失败";
        String DEL_ERROR="从连接池获取连接失败或者从缓存删除失败";
        String EXPIRE_ERROR = "从连接池获取连接失败或者设置时间失败";
        String KEYS_ERROR = "从连接池获取连接失败或者遍历失败";
        String MARK_SESSION_EXCEPTION = "标记重复用户失败";
        String FORCE_LOGOUT_FAILED = "用户踢出，强制logout失败";

    }
}
