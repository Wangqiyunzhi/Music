package com.rabbiter.music.service.impl;

import com.rabbiter.music.dao.CollectMapper;
import com.rabbiter.music.pojo.Collect;
import com.rabbiter.music.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectMapper collectMapper;

    @Override
    public boolean insert(Collect collect) {
        return collectMapper.insert(collect) > 0;
    }

    @Override
    public boolean existSongId(Integer userId, Integer songId) {
        return collectMapper.existSongId(userId, songId) > 0;
    }

    @Override
    public boolean existSongListId(Integer userId, Integer songListId) {
        return collectMapper.existSongListId(userId, songListId) > 0;
    }

    @Override
    public boolean deleteByUserIdSongId(Integer userId, Integer songId) {
        return collectMapper.deleteByUserIdSongId(userId, songId) > 0;
    }

    @Override
    public boolean deleteByUserIdSongListId(Integer userId, Integer songListId) {
        return collectMapper.deleteByUserIdSongListId(userId, songListId) > 0;
    }

    @Override
    public List<Collect> collectOfUserId(Integer userId) {
        return collectMapper.collectOfUserId(userId);
    }

    @Override
    public List<Collect> collectOfUserIdAndType(Integer userId, Integer type) {
        return collectMapper.collectOfUserIdAndType(userId, type);
    }
}


