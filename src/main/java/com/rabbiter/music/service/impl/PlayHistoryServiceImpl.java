package com.rabbiter.music.service.impl;

import com.rabbiter.music.dao.PlayHistoryMapper;
import com.rabbiter.music.pojo.PlayHistory;
import com.rabbiter.music.service.PlayHistoryService;
import com.rabbiter.music.vo.PlayHistoryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PlayHistoryServiceImpl implements PlayHistoryService {

    @Autowired
    private PlayHistoryMapper playHistoryMapper;

    @Override
    public boolean insert(PlayHistory history) {
        return playHistoryMapper.insert(history) > 0;
    }

    @Override
    public boolean deleteByUserId(Integer userId) {
        return playHistoryMapper.deleteByUserId(userId) > 0;
    }

    @Override
    public List<PlayHistoryItem> historyOfUser(Integer userId, Integer limit) {
        if (userId == null) return Collections.emptyList();
        if (limit == null || limit <= 0) limit = 50;
        return playHistoryMapper.historyOfUser(userId, limit);
    }
}


