package com.rabbiter.music.dao;

import com.rabbiter.music.pojo.Admin;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper {
    int verifyPassword(String username, String password);

    Admin selectByName(String name);

    int update(Admin admin);
}


