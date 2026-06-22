package com.rabbiter.music.service.impl;

import com.rabbiter.music.dao.SongListMapper;
import com.rabbiter.music.pojo.SongList;
import com.rabbiter.music.service.SongListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 姝屽崟service瀹炵幇绫?
 */
@Service
public class SongListServiceImpl implements SongListService {

    @Autowired
    private SongListMapper songListMapper;


    /**
     * 澧炲姞
     *
     * @param songList
     */
    @Override
    public boolean insert(SongList songList) {
        return songListMapper.insert(songList)>0;
    }

    /**
     * 淇敼
     *
     * @param songList
     */
    @Override
    public boolean update(SongList songList) {
        return songListMapper.update(songList)>0;
    }

    /**
     * 鍒犻櫎
     *
     * @param id
     */
    @Override
    public boolean delete(Integer id) {
        return songListMapper.delete(id)>0;
    }

    /**
     * 鏍规嵁涓婚敭鏌ヨ鏁翠釜瀵硅薄
     *
     * @param id
     */
    @Override
    public SongList selectByPrimaryKey(Integer id) {
        return songListMapper.selectByPrimaryKey(id);
    }

    /**
     * 鏌ヨ鎵€鏈夋瓕鍗?
     */
    @Override
    public List<SongList> allSongList() {
        return songListMapper.allSongList();
    }

    /**
     * 鏍规嵁鏍囬绮剧‘鏌ヨ姝屽崟鍒楄〃
     *
     * @param title
     */
    @Override
    public List<SongList> songListOfTitle(String title) {
        return songListMapper.songListOfTitle(title);
    }

    /**
     * 鏍规嵁鏍囬妯＄硦鏌ヨ姝屽崟鍒楄〃
     *
     * @param title
     */
    @Override
    public List<SongList> likeTitle(String title) {
        return songListMapper.likeTitle(title);
    }

    /**
     * 鏍规嵁椋庢牸妯＄硦鏌ヨ姝屽崟鍒楄〃
     *
     * @param style
     */
    @Override
    public List<SongList> likeStyle(String style) {
        return songListMapper.likeStyle(style);
    }
}


