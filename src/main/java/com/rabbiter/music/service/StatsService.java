package com.rabbiter.music.service;

import com.rabbiter.music.vo.StatsDateItem;
import com.rabbiter.music.vo.StatsSingerItem;
import com.rabbiter.music.vo.StatsSongItem;

import java.util.List;

public interface StatsService {
    List<StatsSongItem> hotSongs(Integer limit);
    List<StatsSingerItem> hotSingers(Integer limit);
    List<StatsSongItem> playRank(Integer limit);
    List<StatsSongItem> collectRank(Integer limit);
    List<StatsDateItem> dailyNewUsers(Integer days);
}


