package com.rabbiter.music.config;

import com.rabbiter.music.utils.PathUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 瀹氫綅鍚勭鏂囦欢鎴栧ご鍍忓湴鍧€
 */
@Configuration
public class FileConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        //姝屾墜澶村儚鍦板潃
        registry.addResourceHandler("/img/singerPic/**").addResourceLocations(
                "file:///" + PathUtils.getClassLoadRootPath() + "/img/singerPic/"
        );
        //姝屽崟鍥剧墖鍦板潃
        registry.addResourceHandler("/img/songListPic/**").addResourceLocations(
                "file:///" + PathUtils.getClassLoadRootPath() + "/img/songListPic/"
        );
        //姝屾洸鍥剧墖鍦板潃
        registry.addResourceHandler("/img/songPic/**").addResourceLocations(
                "file:///" + PathUtils.getClassLoadRootPath() + "/img/songPic/"
        );
        //姝屾洸鍦板潃
        registry.addResourceHandler("/song/**").addResourceLocations(
                "file:///" + PathUtils.getClassLoadRootPath()+"/song/"
        );
        //鍓嶇鐢ㄦ埛澶村儚鍦板潃
        registry.addResourceHandler("/userImages/**").addResourceLocations(
                "file:///" + PathUtils.getClassLoadRootPath() + "/userImages/"
        );
        //鐢ㄦ埛澶村儚榛樿鍦板潃
        registry.addResourceHandler("/img/**").addResourceLocations(
                "file:///" + PathUtils.getClassLoadRootPath() + "/img/"
        );
    }
}



















