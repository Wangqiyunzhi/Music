package com.rabbiter.music.dao;

import com.rabbiter.music.pojo.Rank;
import org.springframework.stereotype.Repository;

/**
 * 璇勪环Dao
 */
@Repository
public interface RankMapper {
    /**
     *澧炲姞
     */
    public int insert(Rank rank);

    /**
     * 鏌ユ€诲垎
     */
    public int selectScoreSum(Integer songListId);

    /**
     * 鏌ユ€昏瘎鍒嗕汉鏁?
     */
    public int selectRankNum(Integer songListId);

 public Rank selectBySongListIdAndConsumerId(Integer songListId, Integer consumerId);

  public   int updateScoreBySongListIdAndConsumerId(Integer score, Integer songListId, Integer consumerId);


}


