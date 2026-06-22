package com.rabbiter.music.service;

import com.rabbiter.music.pojo.Singer;

import java.util.List;

/**
 * 姝屾墜service鎺ュ彛
 */
public interface SingerService {
    /**
     *澧炲姞
     */
    public boolean insert(Singer singer);

    /**
     *淇敼
     */
    public boolean update(Singer singer);

    /**
     * 鍒犻櫎
     */
    public boolean delete(Integer id);

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


