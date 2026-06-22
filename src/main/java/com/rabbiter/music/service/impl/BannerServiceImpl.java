package com.rabbiter.music.service.impl;

import com.rabbiter.music.dao.BannerMapper;
import com.rabbiter.music.pojo.Banner;
import com.rabbiter.music.service.BannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public boolean insert(Banner banner) {
        return bannerMapper.insert(banner) > 0;
    }

    @Override
    public boolean update(Banner banner) {
        return bannerMapper.update(banner) > 0;
    }

    @Override
    public boolean delete(Integer id) {
        return bannerMapper.delete(id) > 0;
    }

    @Override
    public Banner selectByPrimaryKey(Integer id) {
        return bannerMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Banner> allBanner() {
        return bannerMapper.allBanner();
    }

    @Override
    public List<Banner> bannerOfStatus(Integer status) {
        return bannerMapper.bannerOfStatus(status);
    }

    @Override
    public List<Banner> bannerForHome() {
        return bannerMapper.bannerForHome();
    }
}


