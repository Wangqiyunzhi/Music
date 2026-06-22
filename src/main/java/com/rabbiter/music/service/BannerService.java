package com.rabbiter.music.service;

import com.rabbiter.music.pojo.Banner;
import java.util.List;

public interface BannerService {

    boolean insert(Banner banner);

    boolean update(Banner banner);

    boolean delete(Integer id);

    Banner selectByPrimaryKey(Integer id);

    List<Banner> allBanner();

    List<Banner> bannerOfStatus(Integer status);

    /** 鍓嶅彴棣栭〉杞挱锛氬彧鍙栦笂鏋?status=1)锛屾寜 sort asc,id desc */
    List<Banner> bannerForHome();
}


