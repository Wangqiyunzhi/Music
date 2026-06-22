package com.rabbiter.music.dao;

import com.rabbiter.music.pojo.Collect;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectMapper {

    int insert(Collect collect);

    /** 鉁?姝屾洸鏀惰棌鏄惁瀛樺湪 */
    int existSongId(@Param("userId") Integer userId, @Param("songId") Integer songId);

    /** 鉁?姝屽崟鏀惰棌鏄惁瀛樺湪 */
    int existSongListId(@Param("userId") Integer userId, @Param("songListId") Integer songListId);

    /** 鉁?鍙栨秷鏀惰棌姝屾洸 */
    int deleteByUserIdSongId(@Param("userId") Integer userId, @Param("songId") Integer songId);

    /** 鉁?鍙栨秷鏀惰棌姝屽崟 */
    int deleteByUserIdSongListId(@Param("userId") Integer userId, @Param("songListId") Integer songListId);

    /** 鉁?鏌ョ敤鎴锋墍鏈夋敹钘?*/
    List<Collect> collectOfUserId(@Param("userId") Integer userId);

    /** 鉁?鏌ョ敤鎴锋煇涓€绫绘敹钘忥紙0姝屾洸 / 1姝屽崟锛?*/
    List<Collect> collectOfUserIdAndType(@Param("userId") Integer userId, @Param("type") Integer type);
}


