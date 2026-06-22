package com.rabbiter.music.controller;

import com.alibaba.fastjson.JSONObject;
import com.rabbiter.music.pojo.ListSong;
import com.rabbiter.music.service.ListSongService;
import com.rabbiter.music.service.SongService;
import com.rabbiter.music.utils.Consts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * ListSong controller
 */
@RestController
@RequestMapping("/listSong")
@Api(tags = "ListSong")
public class ListSongController {

    @Autowired
    private ListSongService listSongService;
    @Autowired
    private SongService songService;

    /**
     * Add song to list
     */
    @ApiOperation(value = "Add song to list")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object addListSong(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String songId = request.getParameter("songId");
        String songListId = request.getParameter("songListId");

        if (songId == null || songListId == null) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "songId and songListId required");
            return jsonObject;
        }

        ListSong listSong = new ListSong();
        listSong.setSongId(Integer.parseInt(songId.trim()));
        listSong.setSongListId(Integer.parseInt(songListId.trim()));

        boolean flag = listSongService.insert(listSong);
        if (flag) {
            jsonObject.put(Consts.CODE, 1);
            jsonObject.put(Consts.MSG, "save success");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE, 0);
        jsonObject.put(Consts.MSG, "save failed");
        return jsonObject;
    }

    /**
     * List songs by songListId
     */
    @ApiOperation(value = "List songs by songListId")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public Object detail(HttpServletRequest request) {
        String songListId = request.getParameter("songListId");
        return listSongService.listSongOfSongListId(Integer.parseInt(songListId));
    }

    /**
     * Delete song from list
     */
    @ApiOperation(value = "Delete song from list")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object delete(HttpServletRequest request) {
        String songId = request.getParameter("songId");
        String songListId = request.getParameter("songListId");
        if (songId == null || songListId == null) return false;

        Integer sid = Integer.parseInt(songId.trim());
        Integer slId = Integer.parseInt(songListId.trim());

        // 1) 删除歌单-歌曲关系
        listSongService.deleteBySongIdAndSongListId(sid, slId);
        // 2) 同时清理该 song 在其他歌单中的关系
        listSongService.deleteBySongId(sid);
        // 3) 删除 song 表中的歌曲
        return songService.delete(sid);
    }
}
