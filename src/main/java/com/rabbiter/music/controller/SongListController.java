package com.rabbiter.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.rabbiter.music.pojo.SongList;
import com.rabbiter.music.service.SongListService;
import com.rabbiter.music.utils.Consts;
import com.rabbiter.music.utils.PathUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/songList")
@Api(tags = "SongList")
public class SongListController {

    @Autowired
    private SongListService songListService;

    private String getParam(HttpServletRequest request, String key) {
        String value = request.getParameter(key);
        return value == null ? "" : value.trim();
    }

    @ApiOperation(value = "Add song list")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addSongList(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String title = getParam(request, "title");
        String pic = getParam(request, "pic");
        String introduction = getParam(request, "introduction");
        String style = getParam(request, "style");

        if (title.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "title required");
            return jsonObject;
        }
        if (pic.isEmpty()) {
            pic = "/img/songlist-default.jpg";
        }

        SongList songList = new SongList();
        songList.setTitle(title);
        songList.setPic(pic);
        songList.setIntroduction(introduction);
        songList.setStyle(style);

        boolean flag = songListService.insert(songList);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "add success");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "add failed");
        return jsonObject;
    }

    @ApiOperation(value = "Update song list")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Object updateSongList(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String idText = getParam(request, "id");
        String title = getParam(request, "title");
        String introduction = getParam(request, "introduction");
        String style = getParam(request, "style");

        if (idText.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "song list id required");
            return jsonObject;
        }
        if (title.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "title required");
            return jsonObject;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "song list id format error");
            return jsonObject;
        }

        SongList songList = new SongList();
        songList.setId(id);
        songList.setTitle(title);
        songList.setIntroduction(introduction);
        songList.setStyle(style);

        boolean flag = songListService.update(songList);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "update success");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "update failed");
        return jsonObject;
    }

    @ApiOperation(value = "Delete song list")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object deleteSongList(HttpServletRequest request) {
        String idText = getParam(request, "id");
        if (idText.isEmpty()) {
            return false;
        }
        return songListService.delete(Integer.parseInt(idText));
    }

    @ApiOperation(value = "Query song list by id")
    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request) {
        String idText = getParam(request, "id");
        return songListService.selectByPrimaryKey(Integer.parseInt(idText));
    }

    @ApiOperation(value = "Query all song lists")
    @RequestMapping(value = "/allSongList", method = RequestMethod.GET)
    public Object allSongList(HttpServletRequest request) {
        return songListService.allSongList();
    }

    @ApiOperation(value = "Query song list by title")
    @RequestMapping(value = "/songListOfTitle", method = RequestMethod.GET)
    public Object songListOfName(HttpServletRequest request) {
        String title = getParam(request, "title");
        return songListService.songListOfTitle(title);
    }

    @ApiOperation(value = "Fuzzy query song list by title")
    @RequestMapping(value = "/likeTitle", method = RequestMethod.GET)
    public Object likeTitle(HttpServletRequest request) {
        String title = getParam(request, "title");
        return songListService.likeTitle("%" + title + "%");
    }

    @ApiOperation(value = "Fuzzy query song list by style")
    @RequestMapping(value = "/likeStyle", method = RequestMethod.GET)
    public Object likeStyle(HttpServletRequest request) {
        String style = getParam(request, "style");
        return songListService.likeStyle("%" + style + "%");
    }

    @ApiOperation(value = "Update song list image")
    @RequestMapping(value = "/updateSongListPic", method = RequestMethod.POST)
    public Object updateSongListPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id") int id) {
        JSONObject jsonObject = new JSONObject();
        if (avatorFile.isEmpty()) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "upload failed");
            return jsonObject;
        }

        String fileName = System.currentTimeMillis() + avatorFile.getOriginalFilename();
        String filePath = PathUtils.getClassLoadRootPath() + "/img/songListPic/";
        File folder = new File(filePath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File dest = new File(filePath + fileName);
        String storePath = "/img/songListPic/" + fileName;
        try {
            avatorFile.transferTo(dest);
            SongList songList = new SongList();
            songList.setId(id);
            songList.setPic(storePath);
            boolean flag = songListService.update(songList);
            if (flag) {
                jsonObject.put(Consts.CODE, 1);
                jsonObject.put(Consts.MSG, "upload success");
                jsonObject.put("pic", storePath);
                return jsonObject;
            }
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "upload failed");
            return jsonObject;
        } catch (IOException e) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "upload failed: " + e.getMessage());
            return jsonObject;
        }
    }
}

