package com.exchange.web.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exchange.web.info.SysUser;
import com.exchange.web.service.SysUserService;

/**
 * @Author: ouyangan
 * @Date : 2016/10/7
 */
@Service
@Transactional
public class SysUserServiceImpl implements SysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);
    
    @Override
    public long insertUser(SysUser user, String jobIds, String permissionIds)
    {
        return 0;
    }
    
    @Override
    public boolean isExistLoginName(String loginName)
    {
        return false;
    }
    
    @Override
    public void deleteById(long id)
    {
    }
    
    @Override
    public SysUser selectById(long id)
    {
        return null;
    }
    
    @Override
    public boolean isExistLoginNameExcludeId(long id, String loginName)
    {
        return false;
    }
    
    @Override
    public void updateUser(SysUser user, String jobIds, String permissionIds)
    {
    }
    
    @Override
    public void updateUser(SysUser user)
    {
    }
    
    @Override
    public SysUser selectByLoginName(String loginName)
    {
        return null;
    }
    
}
