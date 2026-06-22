package com.rabbiter.music.service.impl;

import com.rabbiter.music.dao.StatsMapper;
import com.rabbiter.music.service.StatsService;
import com.rabbiter.music.vo.StatsDateItem;
import com.rabbiter.music.vo.StatsSingerItem;
import com.rabbiter.music.vo.StatsSongItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private StatsMapper statsMapper;

    @Override
    public List<StatsSongItem> hotSongs(Integer limit) {
        return statsMapper.hotSongs(limit);
    }

    @Override
    public List<StatsSingerItem> hotSingers(Integer limit) {
        return statsMapper.hotSingers(limit);
    }

    @Override
    public List<StatsSongItem> playRank(Integer limit) {
        return statsMapper.playRank(limit);
    }

    @Override
    public List<StatsSongItem> collectRank(Integer limit) {
        return statsMapper.collectRank(limit);
    }

    @Override
    public List<StatsDateItem> dailyNewUsers(Integer days) {
        return statsMapper.dailyNewUsers(days);
    }
}


