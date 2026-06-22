package com.rabbiter.music.dao;

import com.rabbiter.music.vo.StatsDateItem;
import com.rabbiter.music.vo.StatsSingerItem;
import com.rabbiter.music.vo.StatsSongItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatsMapper {
    List<StatsSongItem> hotSongs(@Param("limit") Integer limit);
    List<StatsSingerItem> hotSingers(@Param("limit") Integer limit);
    List<StatsSongItem> playRank(@Param("limit") Integer limit);
    List<StatsSongItem> collectRank(@Param("limit") Integer limit);
    List<StatsDateItem> dailyNewUsers(@Param("days") Integer days);
}


