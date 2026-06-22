package com.rabbiter.music.service.impl;

import com.rabbiter.music.dao.CommentMapper;
import com.rabbiter.music.pojo.Comment;
import com.rabbiter.music.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public boolean insert(Comment comment) {
        return commentMapper.insert(comment) > 0;
    }

    @Override
    public boolean deleteByIdAndUserId(Integer id, Integer userId) {
        return commentMapper.deleteByIdAndUserId(id, userId) > 0;
    }

    @Override
    public int countByTarget(Integer type, Integer songId, Integer songListId) {
        return commentMapper.countByTarget(type, songId, songListId);
    }

    @Override
    public List<Comment> selectPage(Integer type, Integer songId, Integer songListId, Integer pageNo, Integer pageSize) {
        int pNo = (pageNo == null || pageNo < 1) ? 1 : pageNo;
        int pSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        int offset = (pNo - 1) * pSize;
        return commentMapper.selectPage(type, songId, songListId, pSize, offset);
    }
}


