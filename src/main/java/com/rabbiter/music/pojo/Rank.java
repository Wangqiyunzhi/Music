package com.rabbiter.music.pojo;

import java.io.Serializable;

public class Rank implements Serializable {
    private Integer id;
    private Integer songListId;
    private Integer consumerId;
    private Integer score;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSongListId() {
        return songListId;
    }

    public void setSongListId(Integer songListId) {
        this.songListId = songListId;
    }

    public Integer getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(Integer consumerId) {
        this.consumerId = consumerId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "id=" + id +
                ", songListId=" + songListId +
                ", consumerId=" + consumerId +
                ", score=" + score +
                '}';
    }
}


