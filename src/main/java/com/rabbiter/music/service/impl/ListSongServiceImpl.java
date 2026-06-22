package com.rabbiter.music.service.impl;

import com.rabbiter.music.pojo.ListSong;
import com.rabbiter.music.service.ListSongService;
import com.rabbiter.music.dao.ListSongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 姝屽崟閲岄潰鐨勬瓕鏇瞫ervice瀹炵幇绫?
 */
@Service
public class ListSongServiceImpl implements ListSongService {
    @Autowired
    private ListSongMapper listSongMapper;
    /**
     * 澧炲姞
     *
     * @param listSong
     */
    @Override
    public boolean insert(ListSong listSong) {
        return listSongMapper.insert(listSong)>0;
    }

    /**
     * 淇敼
     *
     * @param listSong
     */
    @Override
    public boolean update(ListSong listSong) {
        return listSongMapper.update(listSong)>0;
    }

    /**
     * 鍒犻櫎
     *
     * @param id
     */
    @Override
    public boolean delete(Integer id) {
        return listSongMapper.delete(id)>0;
    }

    /**
     * 鏍规嵁姝屾洸id鍜屾瓕鍗昳d鍒犻櫎
     */
    @Override
    public boolean deleteBySongIdAndSongListId(Integer songId,Integer songListId){
        return listSongMapper.deleteBySongIdAndSongListId(songId,songListId)>0;
    }

    @Override
    public boolean deleteBySongId(Integer songId){
        return listSongMapper.deleteBySongId(songId)>0;
    }

    /**
     * 鏍规嵁涓婚敭鏌ヨ鏁翠釜瀵硅薄
     *
     * @param id
     */
    @Override
    public ListSong selectByPrimaryKey(Integer id) {
        return listSongMapper.selectByPrimaryKey(id);
    }

    /**
     * 鏌ヨ鎵€鏈夋瓕鍗曢噷闈㈢殑姝屾洸
     */
    @Override
    public List<ListSong> allListSong() {
        return listSongMapper.allListSong();
    }

    /**
     * 鏍规嵁姝屽崟id鏌ヨ鎵€鏈夌殑姝屾洸
     *
     * @param songListId
     */
    @Override
    public List<ListSong> listSongOfSongListId(Integer songListId) {
        return listSongMapper.listSongOfSongListId(songListId);
    }
}


