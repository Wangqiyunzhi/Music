package com.rabbiter.music.dao;

import com.rabbiter.music.pojo.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentMapper {

    int insert(Comment comment);

    // 鉁?鍒犻櫎鑷繁鐨勮瘎璁猴細id + userId
    int deleteByIdAndUserId(@Param("id") Integer id, @Param("userId") Integer userId);

    // 鉁?鍒嗛〉锛氬厛 count
    int countByTarget(@Param("type") Integer type,
                      @Param("songId") Integer songId,
                      @Param("songListId") Integer songListId);

    // 鉁?鍒嗛〉锛氬啀鏌ュ垪琛?
    List<Comment> selectPage(@Param("type") Integer type,
                             @Param("songId") Integer songId,
                             @Param("songListId") Integer songListId,
                             @Param("limit") Integer limit,
                             @Param("offset") Integer offset);
}


