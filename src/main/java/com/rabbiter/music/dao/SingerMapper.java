package com.rabbiter.music.dao;

import com.rabbiter.music.pojo.Singer;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 姝屾墜Dao
 */
@Repository
public interface SingerMapper {
    /**
     *澧炲姞
     */
    public int insert(Singer singer);

    /**
     *淇敼
     */
    public int update(Singer singer);

    /**
     * 鍒犻櫎
     */
    public int delete(Integer id);

    /**
     * 鏍规嵁涓婚敭鏌ヨ鏁翠釜瀵硅薄
     */
    public Singer selectByPrimaryKey(Integer id);

    /**
     * 鏌ヨ鎵€鏈夋瓕鎵?
     */
    public List<Singer> allSinger();

    /**
     * 鏍规嵁姝屾墜鍚嶅瓧妯＄硦鏌ヨ鍒楄〃
     */
    public List<Singer> singerOfName(String name);

    /**
     * 鏍规嵁鎬у埆鏌ヨ
     */
    public List<Singer> singerOfSex(Integer sex);
}


