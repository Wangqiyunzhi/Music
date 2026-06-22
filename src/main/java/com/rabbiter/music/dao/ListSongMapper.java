package com.rabbiter.music.dao;

import com.rabbiter.music.pojo.ListSong;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 姝屽崟閲岄潰鐨勬瓕鏇睤ao
 */
@Repository
public interface ListSongMapper {
    /**
     *澧炲姞
     */
    public int insert(ListSong listSong);

    /**
     *淇敼
     */
    public int update(ListSong listSong);

    /**
     * 鍒犻櫎
     */
    public int delete(Integer id);

    /**
     * 鏍规嵁姝屾洸id鍜屾瓕鍗昳d鍒犻櫎
     */
    public int deleteBySongIdAndSongListId(Integer songId,Integer songListId);

    public int deleteBySongId(Integer songId);

    /**
     * 鏍规嵁涓婚敭鏌ヨ鏁翠釜瀵硅薄
     */
    public ListSong selectByPrimaryKey(Integer id);

    /**
     * 鏌ヨ鎵€鏈夋瓕鍗曢噷闈㈢殑姝屾洸
     */
    public List<ListSong> allListSong();

    /**
     * 鏍规嵁姝屽崟id鏌ヨ鎵€鏈夌殑姝屾洸
     */
    public List<ListSong> listSongOfSongListId(Integer songListId);
}


