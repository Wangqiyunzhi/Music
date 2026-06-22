package com.rabbiter.music.service;

import com.rabbiter.music.pojo.Collect;
import java.util.List;

public interface CollectService {

    boolean insert(Collect collect);

    boolean existSongId(Integer userId, Integer songId);

    boolean existSongListId(Integer userId, Integer songListId);

    boolean deleteByUserIdSongId(Integer userId, Integer songId);

    boolean deleteByUserIdSongListId(Integer userId, Integer songListId);

    List<Collect> collectOfUserId(Integer userId);

    List<Collect> collectOfUserIdAndType(Integer userId, Integer type);
}


