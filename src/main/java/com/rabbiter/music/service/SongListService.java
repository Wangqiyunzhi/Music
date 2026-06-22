package com.rabbiter.music.service;

import com.rabbiter.music.pojo.SongList;

import java.util.List;

/**
 * 姝屽崟service鎺ュ彛
 */
public interface SongListService {
    /**
     *澧炲姞
     */
    public boolean insert(SongList songList);

    /**
     *淇敼
     */
    public boolean update(SongList songList);

    /**
     * 鍒犻櫎
     */
    public boolean delete(Integer id);

    /**
     * 鏍规嵁涓婚敭鏌ヨ鏁翠釜瀵硅薄
     */
    public SongList selectByPrimaryKey(Integer id);

    /**
     * 鏌ヨ鎵€鏈夋瓕鍗?
     */
    public List<SongList> allSongList();

    /**
     * 鏍规嵁鏍囬绮剧‘鏌ヨ姝屽崟鍒楄〃
     */
    public List<SongList> songListOfTitle(String title);

    /**
     * 鏍规嵁鏍囬妯＄硦鏌ヨ姝屽崟鍒楄〃
     */
    public List<SongList> likeTitle(String title);

    /**
     * 鏍规嵁椋庢牸妯＄硦鏌ヨ姝屽崟鍒楄〃
     */
    public List<SongList> likeStyle(String style);

}


