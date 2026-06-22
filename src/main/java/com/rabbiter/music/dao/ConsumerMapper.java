package com.rabbiter.music.dao;

import com.rabbiter.music.pojo.Consumer;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 鍓嶇鐢ㄦ埛Dao
 */
@Repository
public interface ConsumerMapper {
    /**
     *澧炲姞
     */
    public int insert(Consumer consumer);

    /**
     *淇敼
     */
    public int update(Consumer consumer);

    /**
     * 鍒犻櫎
     */
    public int delete(Integer id);

    /**
     * 鏍规嵁涓婚敭鏌ヨ鏁翠釜瀵硅薄
     */
    public Consumer selectByPrimaryKey(Integer id);

    /**
     * 鏌ヨ鎵€鏈夌敤鎴?
     */
    public List<Consumer> allConsumer();

    /**
     * 楠岃瘉瀵嗙爜
     */
    public int verifyPassword(String username,String password);

    /**
     * 鏍规嵁璐﹀彿鏌ヨ
     */
    public Consumer getByUsername(String username);


}


