package com.rabbiter.music.service;

import com.rabbiter.music.pojo.Admin;

public interface AdminService {
    boolean verifyPassword(String username, String password);

    Admin getByName(String name);

    boolean update(Admin admin);
}


