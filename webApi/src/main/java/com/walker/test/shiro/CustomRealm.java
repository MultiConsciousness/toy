package com.walker.test.shiro;

import com.walker.test.common.SysConstant;
import com.walker.test.dao.entity.Role;
import com.walker.test.dao.entity.User;
import com.walker.test.service.user.IPermissionService;
import com.walker.test.service.user.IRoleService;
import com.walker.test.service.user.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private IUserService userService;
    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private IRoleService roleService;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Subject subject = SecurityUtils.getSubject();
        User user =(User) subject.getPrincipal();
        List<String> pers1= permissionService.findPersByUserId(user.getUserId());
        List<String> pers2 = permissionService.findPersUnderRoleByUserId(user.getUserId());
        Set<String> pers = new HashSet<>();
        pers.addAll(pers1);
        pers.addAll(pers2);
        Role role = roleService.findRoleByUserId(user.getUserId());
        Set<String> roles = new HashSet<>();
        roles.add(role.getRoleName());
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addStringPermissions(pers);
        authorizationInfo.addRoles(roles);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String account =(String) authenticationToken.getPrincipal();
        User user = userService.findUserByAccount(account);
        if(null == user)
            throw new AccountException(SysConstant.AuthenAndAuthor.ACCOUNT_NOT_EXIST);
        if(user.getLock() == SysConstant.AuthenAndAuthor.LOCK_STATUS)
            throw new LockedAccountException(SysConstant.AuthenAndAuthor.LOCKED_MSG);
        String password = user.getPassword();
        String salt = user.getSalt();
        user.setPassword(null);
        user.setAccount(account);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo
                (user,password, ByteSource.Util.bytes(salt),"customRealm");
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute(SysConstant.ShiroConfig.CURRENT_USER,user);
        session.setAttribute(SysConstant.ShiroConfig.CURRENT_USERID,user.getUserId());
        return authenticationInfo;
    }

    /*
    * 清除所有认证信息缓存
    * */
    public void clearAllCachedAuthenticationInfo(){
        getAuthenticationCache().clear();
    }

    /*
    * 清除所有授权信息缓存
    * */
    public void clearAllCachedAuthorizationInfo(){
        getAuthorizationCache().clear();
    }

    /*
    * 清除所有缓存
    * */
    public void clearAllCached(){
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }


}
