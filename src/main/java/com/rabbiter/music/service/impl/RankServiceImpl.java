package com.rabbiter.music.service.impl;

import com.rabbiter.music.pojo.Rank;
import com.rabbiter.music.service.RankService;
import com.rabbiter.music.dao.RankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 璇勪环service瀹炵幇
 */
@Service
public class RankServiceImpl implements RankService {

    @Autowired
    private RankMapper rankMapper;



    /**
     * 鏌ユ€诲垎
     *
     * @param songListId
     */
    @Override
    public int selectScoreSum(Integer songListId) {
        return rankMapper.selectScoreSum(songListId);
    }

    /**
     * 鏌ユ€昏瘎鍒嗕汉鏁?
     *
     * @param songListId
     */
    @Override
    public int selectRankNum(Integer songListId) {
        return rankMapper.selectRankNum(songListId);
    }

    /**
     * 璁＄畻骞冲潎鍒?
     *
     * @param songListId
     */
    @Override
    public int rankOfSongListId(Integer songListId) {
        int rankNum = rankMapper.selectRankNum(songListId);
        if(rankNum==0){
            return 5;
        }
        return rankMapper.selectScoreSum(songListId)/rankNum;
    }

    /**
     * 澧炲姞
     *
     * @param rank
     */
    @Override
    public boolean insert(Rank rank) {
        Rank exist = rankMapper.selectBySongListIdAndConsumerId(rank.getSongListId(), rank.getConsumerId());
        if (exist != null) {
            return rankMapper.updateScoreBySongListIdAndConsumerId(
                    rank.getScore(), rank.getSongListId(), rank.getConsumerId()
            ) > 0;
        }
        return rankMapper.insert(rank) > 0;
    }

}


