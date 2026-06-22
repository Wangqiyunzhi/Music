package com.rabbiter.music.dao;

import com.rabbiter.music.pojo.Banner;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerMapper {

    int insert(Banner banner);

    int update(Banner banner);

    int delete(Integer id);

    Banner selectByPrimaryKey(Integer id);

    List<Banner> allBanner();

    List<Banner> bannerOfStatus(Integer status);

    /** 鍓嶅彴棣栭〉杞挱锛氬彧鍙栦笂鏋?status=1)锛屾寜 sort asc,id desc */
    List<Banner> bannerForHome();
}


