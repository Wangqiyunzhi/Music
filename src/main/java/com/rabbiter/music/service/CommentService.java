package com.rabbiter.music.service;

import com.rabbiter.music.pojo.Comment;

import java.util.List;

public interface CommentService {

    boolean insert(Comment comment);

    boolean deleteByIdAndUserId(Integer id, Integer userId);

    int countByTarget(Integer type, Integer songId, Integer songListId);

    List<Comment> selectPage(Integer type, Integer songId, Integer songListId, Integer pageNo, Integer pageSize);
}


