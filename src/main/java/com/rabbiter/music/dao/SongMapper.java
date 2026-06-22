package com.rabbiter.music.dao;

import com.rabbiter.music.pojo.Song;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongMapper {
    int insert(Song song);

    int update(Song song);

    int delete(Integer id);

    Song selectByPrimaryKey(Integer id);

    List<Song> allSong();

    List<Song> songOfName(String name);

    List<Song> likeSongOfName(String name);

    List<Song> songOfSingerId(Integer singerId);

    int incrPlayCount(Integer id);
}


