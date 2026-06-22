package com.rabbiter.music.service;

import com.rabbiter.music.pojo.PlayHistory;
import com.rabbiter.music.vo.PlayHistoryItem;

import java.util.List;

public interface PlayHistoryService {
    boolean insert(PlayHistory history);

    boolean deleteByUserId(Integer userId);

    List<PlayHistoryItem> historyOfUser(Integer userId, Integer limit);
}


