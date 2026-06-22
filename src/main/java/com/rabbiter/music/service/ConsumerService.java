package com.rabbiter.music.service;

import com.rabbiter.music.pojo.Consumer;

import java.util.List;

/**
 * 鍓嶇鐢ㄦ埛service鎺ュ彛
 */
public interface ConsumerService {
    /**
     *澧炲姞
     */
    public boolean insert(Consumer consumer);

    /**
     *淇敼
     */
    public boolean update(Consumer consumer);

    /**
     * 鍒犻櫎
     */
    public boolean delete(Integer id);

    /**
     * 鏍规嵁涓婚敭鏌ヨ鏁翠釜瀵硅薄
     */
    public Consumer selectByPrimaryKey(Integer id);

    /**
     * 鏌ヨ鎵€鏈夌敤鎴?
     */
    public List<Consumer> allConsumer();

    /**
     * 鏌ョ湅瀵嗙爜鏄惁姝ｇ‘
     */
    public boolean verifyPassword(String username,String password);

    /**
     * 鏍规嵁璐﹀彿鏌ヨ
     */
    public Consumer getByUsername(String username);

}


