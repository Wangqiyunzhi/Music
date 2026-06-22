package com.rabbiter.music.dao;

import com.rabbiter.music.pojo.PlayHistory;
import com.rabbiter.music.vo.PlayHistoryItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayHistoryMapper {
    int insert(PlayHistory history);

    int deleteByUserId(Integer userId);

    List<PlayHistoryItem> historyOfUser(@Param("userId") Integer userId, @Param("limit") Integer limit);
}


