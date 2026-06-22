package com.rabbiter.music.service.impl;

import com.rabbiter.music.service.AdminService;
import com.rabbiter.music.dao.AdminMapper;
import com.rabbiter.music.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 绠＄悊鍛榮ervice瀹炵幇绫?
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 楠岃瘉瀵嗙爜鏄惁姝ｇ‘
     *
     * @param username
     * @param password
     */
    @Override
    public boolean verifyPassword(String username, String password) {
        return adminMapper.verifyPassword(username,password)>0;
    }

    @Override
    public Admin getByName(String name) {
        return adminMapper.selectByName(name);
    }

    @Override
    public boolean update(Admin admin) {
        return adminMapper.update(admin) > 0;
    }
}


