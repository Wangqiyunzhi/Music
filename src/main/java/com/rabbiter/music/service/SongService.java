package com.rabbiter.music.service;

import com.rabbiter.music.pojo.Song;

import java.util.List;
//   设计图纸的地方
public interface SongService {
    boolean insert(Song song);

    boolean update(Song song);

    boolean delete(Integer id);

    Song selectByPrimaryKey(Integer id);

    List<Song> allSong();

    List<Song> songOfName(String name);

    List<Song> likeSongOfName(String name);

    List<Song> songOfSingerId(Integer singerId);

    boolean incrPlayCount(Integer id);
}


