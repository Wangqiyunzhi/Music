package com.rabbiter.music.service;

import com.rabbiter.music.pojo.Rank;

/**
 * 璇勪环service鎺ュ彛
 */
public interface RankService {
    /**
     *澧炲姞
     */
    public boolean insert(Rank rank);

    /**
     * 鏌ユ€诲垎
     */
    public int selectScoreSum(Integer songListId);

    /**
     * 鏌ユ€昏瘎鍒嗕汉鏁?
     */
    public int selectRankNum(Integer songListId);

    /**
     * 璁＄畻骞冲潎鍒?
     */
    public int rankOfSongListId(Integer songListId);
}


